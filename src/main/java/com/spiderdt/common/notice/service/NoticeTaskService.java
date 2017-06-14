package com.spiderdt.common.notice.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.spiderdt.common.notice.common.*;
import com.spiderdt.common.notice.dao.NoticeTasksDao;
import com.spiderdt.common.notice.dao.TasksResultDao;
import com.spiderdt.common.notice.dao.TrackRecodeDao;
import com.spiderdt.common.notice.entity.NoticeTasksEntity;
import com.spiderdt.common.notice.entity.NoticeTasksResultEntity;
import com.spiderdt.common.notice.entity.TrackRecodeEntity;
import com.spiderdt.common.notice.errorhander.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by fivebit on 2017/6/5.
 */
@SuppressWarnings("ALL")
@Service("noticeTaskService")
public class NoticeTaskService {
    @Resource
    private NoticeTasksDao noticeTasksDao;
    @Resource
    private TrackRecodeDao trackRecodeDao;
    @Resource
    private TasksResultDao tasksResultDao;
    @Resource
    private UrlService urlService;
    @Resource
    private SmsService smsService;
    @Resource
    private EmailService emailService;
    @Autowired
    Slog slog;
    protected  String json_address_key = "address"; //phone/email
    protected  String url_ac = "click";
    protected  String track_type = "click";
    protected  String image_track_type = "image";

    /**
     * 通过间隔的扫描数据库状态，获取任务,发送短息/email
     */
    public void sendNoticeTaskByDbScan() throws AppException {
        List<NoticeTasksResultEntity> task_list = tasksResultDao.getNewNoticeTaskResultsBatch(AppConstants.TASK_STATUS_NEW);
        Jlog.info("get task list from db, count:"+task_list.size());
        if(task_list.size() > 0){
            List<NoticeTasksResultEntity> sms_task_list = Lists.newArrayList();
            List<NoticeTasksResultEntity> email_task_list = Lists.newArrayList();
            for(NoticeTasksResultEntity item: task_list) {
                if (item.getTaskType().equals(AppConstants.SMS_TASK_TYPE) == true) {
                    sms_task_list.add(item);
                } else if (item.getTaskType().equals(AppConstants.EMAIL_TASK_TYPE) == true) {
                    email_task_list.add(item);
                }
            }
            Boolean sms_st = smsService.sendSmsBatch(sms_task_list);
            Boolean email_st = emailService.sendEmailBatch(email_task_list);
            Jlog.info("send notice end:sms:"+sms_st+" email:"+email_st);
        }
    }
    /**
     *  创建一个通知task。包括初始化result_info表。
     *  区分不同的任务类型。如email／sms等
     * @param noticeTasksEntity
     * @return
     */
    public Boolean createNoticTask(NoticeTasksEntity noticeTasksEntity) throws AppException {
        slog.debug("create notice task:"+noticeTasksEntity);
        noticeTasksEntity.setParentTaskId(0);
        noticeTasksEntity.setStatus("new");
        noticeTasksEntity.setCreateTime(Jdate.getNowStrTime());
        noticeTasksEntity.setUpdateTime(Jdate.getNowStrTime());
        checkNoticeTaskParams(noticeTasksEntity);
        try {
            //设置地址和用户名
            noticeTasksEntity.setAddresses(getAddressesFromJobId(noticeTasksEntity.getJobId()));
            Integer count = noticeTasksDao.createNoticeTask(noticeTasksEntity);
            if(count !=  1 ){
                slog.error("create notice task entity error:"+noticeTasksEntity);
                throw new AppException("0","create notice task error");
            }
            Integer task_id = noticeTasksEntity.getTaskId();
            slog.debug("create notce task task_id:"+task_id);
            Boolean st_result_info = createTaskResultInfo(task_id,noticeTasksEntity);
            if(st_result_info == false){
                slog.error("create notice task result info error:"+noticeTasksEntity);
            }
        }catch (Exception ee){
            slog.error("create notice task error:"+ee.getMessage());
            throw new AppException("0","create notice task error");
        }
        return true;
    }

    /**
     * 检测创建任务参数
     * @param noticeTasksEntity
     * @return
     */
    public Boolean checkNoticeTaskParams(NoticeTasksEntity noticeTasksEntity){

        return true;
    }

    /**
     * 通过job_id ,获取对应的用户的地址和用户名
     * @param job_id
     * @return
     * name,phone/email json string
     */
    public String getAddressesFromJobId(String job_id){


        String ret = "[{\"name\":\"qiong\",\"address\":\"18217168545\"}]";
        return ret;

    }

    /**
     * 通过任务信息，创建任务的具体信息表notice_tasks_result_info。
     * 存储的是每个地址对应发送的修改后的信息.
     * @param task_id
     * @param noticeTasksEntity
     * @return
     * @throws AppException
     */
    public Boolean createTaskResultInfo(Integer task_id,NoticeTasksEntity noticeTasksEntity) throws AppException {
        try {
            List<NoticeTasksResultEntity> tasksResultEntities =  makeTaskResultInfo(task_id,noticeTasksEntity);
            slog.debug("get task result entities:"+tasksResultEntities);
            saveNoticeResultsBatch(tasksResultEntities);
        }catch ( Exception ee){
            slog.error("create notice task result error:"+ee.getMessage());
            throw new AppException("0","create task result error");
        }
        return true;
    }

    /**
     * 从数据库中存的jsonstring，转换成json object。检验格式。
     * @param addresses
     * @return
     */
    public List<Map<String,String>> getAddresssesFromJsonString(String addresses) throws AppException {
        JSONArray addes = null;
        try {
            addes = JSON.parseArray(addresses);
        }catch (Exception ee){
            slog.error("parse addresses error:"+addresses+" message:"+ee.getMessage());
            throw new AppException("0","parse addresses error:"+addresses);
        }
        List<Map<String, String>> rets = Lists.newArrayList();
        if(addes != null) {
            for (Object add : addes) {
                JSONObject add_json = JSONObject.parseObject(add.toString());
                Set<String> key_set = add_json.keySet();
                if (key_set.contains(this.json_address_key) == false) {
                    slog.error("addresses format is wrong:" + addresses);
                    throw new AppException("0", "address format is error:" + addresses);
                }
                Map<String, String> item = Maps.newHashMap();
                for (String key : key_set) {
                    item.put(key, add_json.getString(key));
                }
                rets.add(item);
            }
        }
        return rets;
    }

    /**
     * 根据notice_tasks_result_info的一条记录，生成对应的notice_tasks_track_recode entitys
     * @param noticeTasksResultEntity
     * @return
     */
    public List<TrackRecodeEntity> makeTrackRecodeInfo(NoticeTasksResultEntity noticeTasksResultEntity){
        //抽取message中所有的url。
        List<String> org_urls = Utils.getUrlsFromMessage(noticeTasksResultEntity.getMessage());
        List<TrackRecodeEntity> tr_lists = Lists.newArrayList();
        for(String org_url:org_urls){
            TrackRecodeEntity tr_entity = new TrackRecodeEntity();
            Map<String,String> trackUrlParam = Maps.newHashMap();
            trackUrlParam.put("dest_url",org_url);
            trackUrlParam.put("task_id",String.valueOf(noticeTasksResultEntity.getTaskId()));
            trackUrlParam.put("riid",noticeTasksResultEntity.getRiid());
            trackUrlParam.put("sc",noticeTasksResultEntity.getTaskType());      //使用任务类型 sms/email
            trackUrlParam.put("ac",this.url_ac);
            Map<String,String> urlPair = urlService.makeTrackUrlPair(trackUrlParam);
            tr_entity.setTrackUrlSuffix(urlPair.get(org_url));
            tr_entity.setTrackType(this.track_type);
            tr_entity.setUrlOrg(org_url);
            tr_entity.setTaskId(noticeTasksResultEntity.getTaskId());
            tr_entity.setRiid(noticeTasksResultEntity.getRiid());
            tr_entity.setMessageOrg(noticeTasksResultEntity.getMessage());
            tr_entity.setParams(trackUrlParam.toString());
            tr_lists.add(tr_entity);
        }
        if(noticeTasksResultEntity.getTaskType().equals(AppConstants.EMAIL_TASK_TYPE) == true){     //需要额外的打开跟踪链接
            TrackRecodeEntity tr_entity = new TrackRecodeEntity();
            Map<String,String> trackUrlParam = Maps.newHashMap();
            String dest_url = "img_track_url:"+noticeTasksResultEntity.getRiid();   //hack
            trackUrlParam.put("dest_url",dest_url);
            trackUrlParam.put("task_id",String.valueOf(noticeTasksResultEntity.getTaskId()));
            trackUrlParam.put("riid",noticeTasksResultEntity.getRiid());
            trackUrlParam.put("sc",noticeTasksResultEntity.getTaskType());      //使用任务类型 sms/email
            trackUrlParam.put("ac",this.url_ac);
            Map<String,String> urlPair = urlService.makeTrackUrlPair(trackUrlParam);
            tr_entity.setTrackUrlSuffix(urlPair.get(dest_url));
            tr_entity.setTrackType(this.image_track_type);
            tr_entity.setUrlOrg(dest_url);
            tr_entity.setTaskId(noticeTasksResultEntity.getTaskId());
            tr_entity.setRiid(noticeTasksResultEntity.getRiid());
            tr_entity.setMessageOrg(noticeTasksResultEntity.getMessage());
            tr_entity.setParams(trackUrlParam.toString());
            tr_lists.add(tr_entity);
        }
        return tr_lists;
    }
    /**
     * 替换message里面的url为跟踪url
     * @param message
     * @param item
     * @return
     */
    public String replaceMessageUrlToTrackUrl(NoticeTasksResultEntity noticeTasksResultEntity,List<TrackRecodeEntity> recodes){
        String message = noticeTasksResultEntity.getMessage();
        for(TrackRecodeEntity recode: recodes){
            String url_org = recode.getUrlOrg();
            String track_url = Jurl.getCompleteTrackUrl(recode.getTrackUrlSuffix());
            message = replaceUrlFromMessage(noticeTasksResultEntity.getTaskType(),message,url_org,track_url);
        }
        return message;
    }

    /**
     * 根据不同的任务类型，替换message中的url。sms，则直接替换。email，则需要保留原来链接作为text，link则变为track url
     * @param task_type
     * @param message
     * @param org_url
     * @param track_url
     * @return
     */
    public String replaceUrlFromMessage(String task_type,String message,String org_url,String track_url){
        String short_url = track_url;
        if(task_type.equals(AppConstants.SMS_TASK_TYPE) == true){
            short_url = urlService.makeShortUrl(track_url);
        }
        if(task_type.equals(AppConstants.EMAIL_TASK_TYPE) == true){
            short_url = "<a target=_blank href=\""+track_url+"\">"+org_url+"</a>";
        }
        message = Utils.replaceUrlFromMessage(message,org_url,short_url);
        return message;
    }

    /**
     * 添加邮件点开时的跟踪链接,邮件逻辑需要实现
     * @param message
     * @return
     */
    public String addOpenTrackUrl(NoticeTasksResultEntity item, List<TrackRecodeEntity> trackRecodeEntities){
        if(item.getTaskType().equals(AppConstants.EMAIL_TASK_TYPE) == true){
            for(TrackRecodeEntity tr: trackRecodeEntities){
               if(tr.getTrackType().equals(this.image_track_type) == true){
                   String track_url = Jurl.getCompleteTrackUrl(tr.getTrackUrlSuffix());
                   track_url = "<img src=\""+track_url+"\" />";
                   return item.getMessage()+ track_url;
               }
            }
        }
        return item.getMessage();
    }

    /**
     * 格式化task_result_info信息
     * 这里包括了创建tasks_track_recode信息
     * @param task_id
     * @param noticeTasksEntity
     * @return
     */
    public List<NoticeTasksResultEntity> makeTaskResultInfo(Integer task_id,NoticeTasksEntity noticeTasksEntity) throws AppException {

        List<Map<String,String>> addresses_map_list = getAddresssesFromJsonString(noticeTasksEntity.getAddresses());
        List<NoticeTasksResultEntity> resultInfoList =  Lists.newArrayList();
        List<TrackRecodeEntity> all_trackRecodeEntities = Lists.newArrayList();
        for(Map<String,String> address_map:addresses_map_list){
            NoticeTasksResultEntity item = new NoticeTasksResultEntity();
            String address = address_map.get(this.json_address_key);
            String message = noticeTasksEntity.getMessage();
            String subject = noticeTasksEntity.getSubject();
            for(Map.Entry<String,String> entry : address_map.entrySet()){
                if (entry.getKey().equals(this.json_address_key) == true){
                    continue;
                }
                message = message.replace("#"+entry.getKey()+"#",entry.getValue());     //模版内容替换,URL还未替换
                subject = (subject== null || subject.isEmpty() == true) ? "":subject.replace("#"+entry.getKey()+"#",entry.getValue());
            }
            item.setRiid(Utils.getUUID());
            item.setTaskId(task_id);
            //trick
            item.setTaskType(noticeTasksEntity.getTaskType());      //主要用于后续流程区分
            item.setAddress(address);
            item.setMessage(message);       //设置替换URL之前的message。用于传递参数给后面流程,后面会用替换trackurl后的message覆盖。
            item.setSubject(subject);       //如果是sms,可以不用设置。
            item.setSendStatus(AppConstants.TASK_RESULT_STATUS_NEW);
            item.setSubmitTime(Jdate.getNowStrTime());
            List<TrackRecodeEntity> trackRecodeEntities = makeTrackRecodeInfo(item);
            all_trackRecodeEntities.addAll(trackRecodeEntities);
            //更换原始URL to track url
            item.setMessage(replaceMessageUrlToTrackUrl(item,trackRecodeEntities));
            //添加结尾隐藏的跟踪链接.邮件需要实现
            item.setMessage( addOpenTrackUrl(item,trackRecodeEntities));
            resultInfoList.add(item );
        }
        slog.debug("get NoticeTasksResultEntity:"+resultInfoList);
        try {
            //这里对 track recode 表数据保存起来。主要是因为参数生成要在这里，也不好传出去。
            saveTrackRecodeBatch(all_trackRecodeEntities);
        }catch(Exception ee){
            slog.error("create track recode batch error:"+ee.getMessage());
            throw new AppException("0","create track recoder error");
        }
        return resultInfoList;
    }
    /**
     * 保存各个address的详细记录到notice_tasks_result_info中
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
                tasksResultDao.createNoticeTaskResultBatch(patch);
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

}
