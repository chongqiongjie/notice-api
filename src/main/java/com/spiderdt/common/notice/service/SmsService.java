package com.spiderdt.common.notice.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.spiderdt.common.notice.common.Jdate;
import com.spiderdt.common.notice.common.Jlog;
import com.spiderdt.common.notice.common.Jurl;
import com.spiderdt.common.notice.common.Utils;
import com.spiderdt.common.notice.dao.NoticeTasksDao;
import com.spiderdt.common.notice.dao.TrackRecodeDao;
import com.spiderdt.common.notice.entity.*;
import com.spiderdt.common.notice.errorhander.AppException;
import com.spiderdt.common.notice.task.DefaultSmsSendTask;
import com.spiderdt.common.notice.task.SmsRunTask;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by fivebit on 2017/5/19.
 */
@Service("smsService")
public class SmsService {

    @Resource
    private NoticeTasksDao noticeTasksDao;
    @Resource
    private ThreadPoolTaskExecutor taskPool;
    @Resource
    private TrackRecodeDao trackRecodeDao;
    @Resource
    private UrlService urlService;

    private String task_type = "sms";
    private String sign = "[知助数据]";
    private String subcode = "";
    /**
     * 通过间隔的扫描数据库状态，获取任务,发送短息
     */
    public void sendSmsByDbScan() throws AppException {
        List<NoticeTasksEntity> task_list = noticeTasksDao.getNewNoticeTasks(task_type);
        Jlog.info("get task list from db, count:"+task_list.size());
        if(task_list.size() > 0){
            for(NoticeTasksEntity item: task_list) {
                Integer task_id = item.getTaskId();
                updateNoticeTaskSatus(task_id,"initing");
                Jlog.info("start init task resutl info:"+task_id);
                List<SmsReqEntity.SmsMsgEntity> smsMsgEntitys = getMsgEntitys(item.getAddresses(),item.getMessage());
                Jlog.info("sms msg entity ok:"+task_id+" list SmsMsgEntity:"+smsMsgEntitys.toString());
                List<NoticeTasksResultEntity> noticeTasksResultEntities = createNoticeResultEntity(
                        task_id,smsMsgEntitys);
                Jlog.info("sms task result info ok:"+task_id+noticeTasksResultEntities.toString());
                noticeTasksResultEntities = initSmsTrackRecode(task_id,noticeTasksResultEntities);
                Jlog.info("sms task result info ok:"+task_id+noticeTasksResultEntities.toString());
                Boolean st = saveNoticeResultsBatch(noticeTasksResultEntities);
                if(st == true) {
                    Jlog.info("save sms task result to db OK:" + task_id);
                    updateNoticeTaskSatus(task_id, "inited");
                    sendMsgToHttpClient(task_id, smsMsgEntitys);
                }else{
                    Jlog.error("save sms task result to db error:"+task_id);
                    updateNoticeTaskSatus(task_id, "failed");
                }
            }
        }
    }

    /**
     * 通过 addresses(json) 获得用户地址列表。根据message，进行替换
     *
     * @param addresses [{"phone":"18217168545","name":"fivebit"}] phone 是关键词不能修改。name 可以随message里面的设定
     * @param message "#name# 您好,很高兴的通知您 【知助数据】"
     * @return
     */

    public List<SmsReqEntity.SmsMsgEntity> getMsgEntitys(String addresses,String message) throws AppException {
        JSONArray addes = null;
        String reserve_key = "phone";
        List<SmsReqEntity.SmsMsgEntity> msgList = new ArrayList<SmsReqEntity.SmsMsgEntity>();
        try {
            addes = JSON.parseArray(addresses);
        }catch (Exception ee){
            Jlog.error("parse addresses error:"+addresses+" message:"+ee.getMessage());
            throw new AppException("0","parse addresses error:"+addresses);
        }
        if(addes != null){
            for(Object add:addes){
                String m_msg = message;
                JSONObject add_json = JSONObject.parseObject(add.toString());
                Set<String> key_set = add_json.keySet();
                if(key_set.contains(reserve_key) == false){
                    Jlog.error("addresses format is wrong:"+addresses);
                    throw new AppException("0","address format is error:"+addresses);
                }
                for(String key:key_set){
                    if(key.equals(reserve_key) == true){
                        continue;
                    }
                    m_msg = m_msg.replace("#"+key+"#",add_json.getString(key));
                }
                SmsReqEntity.SmsMsgEntity smsMsgEntity = new SmsReqEntity.SmsMsgEntity();
                smsMsgEntity.setContent(m_msg);
                smsMsgEntity.setPhones(add_json.getString(reserve_key));
                smsMsgEntity.setMsgid(UUID.randomUUID().toString().replace("-", ""));
                smsMsgEntity.setSign(sign);
                smsMsgEntity.setSendtime("");
                smsMsgEntity.setSubcode(subcode);
                msgList.add(smsMsgEntity);
            }
        }
        return msgList;
    }

    /**
     * 根据输入，创建短信task
     * @param client_id
     * @param user_id
     * @param addresses
     * @param message
     * @return task_id
     */
    public int createSmsTask(String client_id,String user_id,String addresses,String message){
        Jlog.info("create sms task begin");
        int task_id = 0;
        NoticeTasksEntity noticeTasksEntity = new NoticeTasksEntity();
        noticeTasksEntity.setAddresses(addresses);
        noticeTasksEntity.setClientId(client_id);
        noticeTasksEntity.setMessage(message);
        noticeTasksEntity.setUserId(user_id);
        noticeTasksEntity.setTaskType(task_type);
        noticeTasksEntity.setParentTaskId(0);
        noticeTasksEntity.setStatus("new");
        noticeTasksEntity.setCreateTime(Jdate.getNowStrTime());
        noticeTasksEntity.setUpdateTime(Jdate.getNowStrTime());
        try {
            task_id = noticeTasksDao.createNoticeTask(noticeTasksEntity);
        }catch(Exception ee){
            Jlog.error(" create task error:"+ee.getMessage());
        }
        Jlog.info("create task success:"+noticeTasksEntity.toString()+" task_id:"+task_id);

        return task_id;
    }
    public void sendSmsByTaskId(int task_id){
        //NoticeTasksEntity noticeTasksEntity = noticeTasksDao.getNewNoticeTask();
        //log.info("get new task:"+noticeTasksEntity.toString());
        SmsReqEntity smsReqEntity = new SmsReqEntity();
        SmsReqEntity.SmsMsgEntity smsMsgEntity = new SmsReqEntity.SmsMsgEntity();
        smsMsgEntity.setContent("xxx");
        List<SmsReqEntity.SmsMsgEntity> items = new ArrayList<SmsReqEntity.SmsMsgEntity>();
        items.add(smsMsgEntity);
        smsReqEntity.setData(items);
        Jlog.info(smsReqEntity.toString());

    }

    /**
     * 更新任务状态
     * @param task_id
     * @param status new/initing/inited/sending/sended/等
     * @return
     */
    public Boolean updateNoticeTaskSatus(Integer task_id,String status){
        Boolean st = true;
        String update_time = Jdate.getNowStrTime();
        try {
            noticeTasksDao.updateNoticeTaskStatus(task_id, status, update_time);
        }catch(Exception ee){
            st = false;
            Jlog.error("update notice status error:"+ee.getMessage());
        }
        return st;
    }

    /**
     *
     * 逻辑：扣取出message中的链接。加密，并替换。
     * 并把这些数据存放到notice_tasks_track_recode表中
     * 同时，这个加密的串，传出去，会放在result数据表中，作为映射
     * @param task_id
     * @param items
     * @return
     */
    public List<NoticeTasksResultEntity> initSmsTrackRecode(Integer task_id,List<NoticeTasksResultEntity> items){
        List<TrackRecodeEntity> trackRecodeEntities = Lists.newArrayList();
        for(NoticeTasksResultEntity item: items){
            String message = item.getMessage();
            String costomer_url = Utils.getUrlFromMessage(message);
            Map<String,String> trackUrlParam = Maps.newHashMap();
            trackUrlParam.put("dest_url",costomer_url);
            trackUrlParam.put("task_id",String.valueOf(task_id));
            trackUrlParam.put("sc","sms");
            trackUrlParam.put("ac","click");
            Map<String,String> urlPair = urlService.makeTrackUrlPair(trackUrlParam);
            Jlog.info("url pair:"+urlPair);
            String track_url_org = Jurl.getCompleteTrackUrl(urlPair.get(costomer_url));
            Jlog.info("track url org:"+track_url_org);
            String short_track_url = urlService.makeShortUrl(track_url_org);
            Jlog.info("short track url"+short_track_url);
            String message_replace = Utils.replaceUrlFromMessage(message,short_track_url);
            Jlog.debug("message replace"+message_replace);
            TrackRecodeEntity track_entity = new TrackRecodeEntity();
            track_entity.setTaskId(task_id);
            track_entity.setTrackUrlSuffix(urlPair.get(costomer_url));
            track_entity.setUrlOrg(costomer_url);
            track_entity.setMessageOrg(message);
            track_entity.setMessageReplace(message_replace);
            trackRecodeEntities.add(track_entity);
            item.setMessage(message_replace);
            item.setTrackUrlSuffix(urlPair.get(costomer_url));
        }
        trackRecodeDao.addTrackRecodeInfoBatch(trackRecodeEntities);

        return items;
    }


    /**
     * 根据task的addresses创建各个phone的记录entity
     * @param task_id
     * @param items
     * @return
     */
    public List<NoticeTasksResultEntity> createNoticeResultEntity(Integer task_id,
                                                                  List<SmsReqEntity.SmsMsgEntity> items){
        List<NoticeTasksResultEntity> tasksResultEntities = new ArrayList<NoticeTasksResultEntity>(items.size());
        String submit_time = Jdate.getNowStrTime();
        for(SmsReqEntity.SmsMsgEntity msg:items){
            NoticeTasksResultEntity tasksResultEntity = new NoticeTasksResultEntity();
            tasksResultEntity.setTaskId(task_id);
            tasksResultEntity.setAddress(msg.getPhones());
            tasksResultEntity.setMsgid(msg.getMsgid());
            tasksResultEntity.setSubmitTime(submit_time);
            tasksResultEntity.setSendStatus("new");
            tasksResultEntity.setMessage(msg.getContent());
            tasksResultEntities.add(tasksResultEntity);
        }
        return tasksResultEntities;
    }

    /**
     * 保存各个address的详细记录到db中
     * 默认batch :100
     * @param lists
     * @return
     */
    public Boolean saveNoticeResultsBatch(List<NoticeTasksResultEntity> lists) throws AppException {
        Boolean st = true;
        int lsize = lists.size();
        float fsize = 100f;
        int isize = 100;
        int count = (int) Math.ceil(lists.size()/fsize);
        try {
            for (int i = 0; i < count; i++) {
                int end = (i + 1) * isize;
                if (end > lsize) {
                    end = lsize;
                }
                List<NoticeTasksResultEntity> patch = lists.subList(i * isize, end);
                noticeTasksDao.createNoticeTaskResultBatch(patch);
            }
        }catch (Exception ee){
            Jlog.error("save notice result error:"+ee.getMessage());
            throw new AppException("0","save notice result error:"+ee.getMessage());
        }
        return st;
    }

    /**
     * 批量存到数据表notice_tasks_track_recode
     * @param lists
     * @return
     * @throws AppException
     */
    public Boolean saveTrackRecodeBatch(List<TrackRecodeEntity> lists) throws AppException {
        Boolean st = true;
        int lsize = lists.size();
        float fsize = 100f;
        int isize = 100;
        int count = (int) Math.ceil(lists.size()/fsize);
        try {
            for (int i = 0; i < count; i++) {
                int end = (i + 1) * isize;
                if (end > lsize) {
                    end = lsize;
                }
                List<TrackRecodeEntity> patch = lists.subList(i * isize, end);
                trackRecodeDao.addTrackRecodeInfoBatch(patch);
            }
        }catch (Exception ee){
            Jlog.error("save notice result error:"+ee.getMessage());
            throw new AppException("0","save notice result error:"+ee.getMessage());
        }
        return st;
    }

    /**
     * 调用http请求，发送短信
     * @param task_id
     * @param smsMsgEntitys
     * @return
     */
    public Boolean sendMsgToHttpClient(Integer task_id,List<SmsReqEntity.SmsMsgEntity> smsMsgEntitys){
        Jlog.info("call  sms service begin:"+task_id);
        SmsRunTask smsRunTask = new DefaultSmsSendTask(task_id);
        smsRunTask.setMsg(smsMsgEntitys);
        taskPool.execute(smsRunTask);
        Jlog.info("call  sms service end:"+task_id);
        return true;

    }

    /**
     * 根据每个批次发送的短信，更新数据结果
     * @param rets
     * @return
     */
    public Boolean dealSmsResultStatus(Integer task_id,JSONObject rets){
        Jlog.info("deal sms result:"+rets.toJSONString());
        Boolean st = true;
        SmsRespEntity smsRespEntity = JSONObject.toJavaObject(rets,SmsRespEntity.class);
        String send_time = Jdate.getNowStrTime();
        try {
            if (smsRespEntity.getResult().equals("0") == true) {
                if (smsRespEntity.getData().size() > 0) {
                    List<SmsRespEntity.SmsRespDataEntity> items = smsRespEntity.getData();
                    for (SmsRespEntity.SmsRespDataEntity item : items) {
                        noticeTasksDao.updateNoticeTaskResultStatus(task_id,
                                item.getMsgid(),
                                item.getStatus(),
                                item.getDesc(),
                                send_time, send_time);
                    }

                }

            }
        }catch(Exception ee){
            Jlog.error(" update sms result error:"+ee.getMessage());
            st = false;
        }
        return st;
    }
    public Boolean dealSmsResultReportStatus(JSONObject rets){
        SmsReportEntity smsReportEntity = JSONObject.toJavaObject(rets,SmsReportEntity.class);
        Boolean st = true;
        String back_time = Jdate.getNowStrTime();
        try{
            if(smsReportEntity.getResult().equals("0") == true){
                if(smsReportEntity.getReports().size() > 0){
                    List<SmsReportEntity.SmsReportInfoEntity> items = smsReportEntity.getReports();
                    for(SmsReportEntity.SmsReportInfoEntity item:items){
                        noticeTasksDao.updateNoticeTaskBackInfoStatus(item.getMsgid(),
                                item.getStatus(),item.getDesc(),back_time);
                    }
                }
            }
        }catch(Exception ee){
            Jlog.error("deal sms result report error:"+ee.getMessage());
            st = false;
        }
        return st;
    }
}
