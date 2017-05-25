package com.spiderdt.common.notice.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.spiderdt.common.notice.common.Jdate;
import com.spiderdt.common.notice.common.Jlog;
import com.spiderdt.common.notice.dao.NoticeTasksDao;
import com.spiderdt.common.notice.entity.NoticeTasksEntity;
import com.spiderdt.common.notice.entity.NoticeTasksResultEntity;
import com.spiderdt.common.notice.entity.SmsReqEntity;
import com.spiderdt.common.notice.task.DefaultSmsSendTask;
import com.spiderdt.common.notice.task.SmsRunTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by fivebit on 2017/5/19.
 */
@Service("smsService")
public class SmsService {

    private static Logger log = LoggerFactory.getLogger(SmsService.class);
    @Resource
    private NoticeTasksDao noticeTasksDao;
    @Resource
    private ThreadPoolTaskExecutor taskPool;

    private String task_type = "sms";
    /**
     * 通过间隔的扫描数据库状态，获取任务,发送短息
     */
    public void sendSmsByDbScan(){
        String task_type = "sms";
        List<NoticeTasksEntity> task_list = noticeTasksDao.getNewNoticeTasks(task_type);
        Jlog.info("get task list from db: count:"+task_list.size());
        if(task_list.size() > 0){
            for(NoticeTasksEntity item: task_list) {
                updateNoticeTaskSatus(item.getTaskId(),"initing");
                Jlog.info("start init task resutl info"+item.getTaskId());
                SmsReqEntity smsReqEntity = getReqEntity(item.getAddresses(),item.getMessage());
                Jlog.info("sms req entity ok:"+item.getTaskId());
                List<NoticeTasksResultEntity> noticeTasksResultEntities = createNoticeResultEntity(
                        item.getTaskId(),smsReqEntity.getData());
                Jlog.info("sms task result info ok:"+item.getTaskId());
                saveNoticeResultsBatch(noticeTasksResultEntities);
                Jlog.info("save sms task result to db OK:"+item.getTaskId());
                updateNoticeTaskSatus(item.getTaskId(),"inited");
                sendMsgToHttpClient(item.getTaskId(),smsReqEntity);
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

    public SmsReqEntity getReqEntity(String addresses,String message){
        JSONArray addes = null;
        String reserve_key = "phone";
        SmsReqEntity smsReqEntity = new SmsReqEntity();
        List<SmsReqEntity.SmsMsgEntity> msgList = new ArrayList<SmsReqEntity.SmsMsgEntity>();
        try {
            addes = JSON.parseArray(addresses);
        }catch (Exception ee){
            log.error("parse addresses error:"+addresses+" message:"+ee.getMessage());
            return null;
        }
        if(addes != null){
            for(Object add:addes){
                String m_msg = message;
                JSONObject add_json = JSONObject.parseObject(add.toString());
                Set<String> key_set = add_json.keySet();
                if(key_set.contains(reserve_key) == false){
                    log.error("addresses format is wrong:"+addresses);
                    return null;
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
                smsMsgEntity.setSign("【知助数据】");
                smsMsgEntity.setSendtime("");
                smsMsgEntity.setSubcode("");
                msgList.add(smsMsgEntity);
            }
        }
        smsReqEntity.setData(msgList);
        smsReqEntity.setAccout("");
        smsReqEntity.setPassword("");

        return smsReqEntity;
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
        log.info("create sms task begin");
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
            log.error(" create task error:"+ee.getMessage());
        }
        log.info("create task success:"+noticeTasksEntity.toString()+" task_id:"+task_id);

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
        log.info(smsReqEntity.toString());

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
            log.error("update notice status error:"+ee.getMessage());
        }
        return st;
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
            tasksResultEntity.setStatus("new");
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
    public Boolean saveNoticeResultsBatch(List<NoticeTasksResultEntity> lists){
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
            st = false;
        }
        return st;
    }

    /**
     * 调用http请求，发送短信
     * @param task_id
     * @param smsReqEntity
     * @return
     */
    public Boolean sendMsgToHttpClient(Integer task_id,SmsReqEntity smsReqEntity){
        Jlog.info("call  sms service begin:"+task_id);
        SmsRunTask smsRunTask = new DefaultSmsSendTask(task_id);
        smsRunTask.setMsg(smsReqEntity);
        taskPool.execute(smsRunTask);
        Jlog.info("call  sms service end:"+task_id);
        return true;

    }
}
