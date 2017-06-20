package com.spiderdt.common.notice.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.spiderdt.common.notice.common.AppConstants;
import com.spiderdt.common.notice.common.Jdate;
import com.spiderdt.common.notice.common.Jlog;
import com.spiderdt.common.notice.dao.NoticeTasksDao;
import com.spiderdt.common.notice.dao.TasksResultDao;
import com.spiderdt.common.notice.dao.TrackRecodeDao;
import com.spiderdt.common.notice.entity.NoticeTasksResultEntity;
import com.spiderdt.common.notice.entity.SmsReportEntity;
import com.spiderdt.common.notice.entity.SmsReqEntity;
import com.spiderdt.common.notice.entity.SmsRespEntity;
import com.spiderdt.common.notice.errorhander.AppException;
import com.spiderdt.common.notice.task.DefaultSmsSendTask;
import com.spiderdt.common.notice.task.SmsRunTask;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by fivebit on 2017/5/19.
 */
@SuppressWarnings("ALL")
@Service("smsService")
public class SmsService extends  NoticeTaskService {

    @Resource
    private NoticeTasksDao noticeTasksDao;
    @Resource
    private TasksResultDao tasksResultDao;
    @Resource
    private ThreadPoolTaskExecutor taskPool;
    @Resource
    private TrackRecodeDao trackRecodeDao;
    @Resource
    private UrlService urlService;

    private String task_type = "sms";
    private String sign = "【知助数据】";
    private String subcode = "";

    /**
     * 批量发送短信
     * @param items
     * @return
     */
    public Boolean sendSmsBatch(List<NoticeTasksResultEntity> items) throws AppException {
        List<SmsReqEntity.SmsMsgEntity> smsMsgEntitys = makeMsgEntitys(items);
        sendMsgToHttpClient( smsMsgEntitys);
        return true;
    }

    /**
     * 通过 addresses(json) 获得用户地址列表。根据message，进行替换
     *
     * @param addresses [{"phone":"18217168545","name":"fivebit"}] phone 是关键词不能修改。name 可以随message里面的设定
     * @param message "#name# 您好,很高兴的通知您 【知助数据】"
     * @return
     */

    public List<SmsReqEntity.SmsMsgEntity> makeMsgEntitys(List<NoticeTasksResultEntity> items) throws AppException {
        JSONArray addes = null;
        String reserve_key = "phone";
        List<SmsReqEntity.SmsMsgEntity> msgList = Lists.newArrayList();
        for(NoticeTasksResultEntity item: items){
            SmsReqEntity.SmsMsgEntity smsMsgEntity = new SmsReqEntity.SmsMsgEntity();
            smsMsgEntity.setContent(item.getMessage());
            smsMsgEntity.setPhones(item.getAddress());
            smsMsgEntity.setMsgid(item.getRiid());
            smsMsgEntity.setSign(sign);
            smsMsgEntity.setSendtime("");
            smsMsgEntity.setSubcode(subcode);
            msgList.add(smsMsgEntity);
        }
        return msgList;
    }





    /**
     * 调用http请求，发送短信
     * @param task_id
     * @param smsMsgEntitys
     * @return
     */
    public Boolean sendMsgToHttpClient(List<SmsReqEntity.SmsMsgEntity> smsMsgEntitys){
        Jlog.info("call  sms service begin");
        SmsRunTask smsRunTask = new DefaultSmsSendTask();
        smsRunTask.setMsg(smsMsgEntitys);
        taskPool.execute(smsRunTask);
        Jlog.info("call  sms service end");
        return true;

    }

    /**
     * 根据每个批次发送的短信，更新数据结果
     * @param send_patch 发送的消息结构体
     * @param http_rets 发送信息的返回信息结构体
     * @return
     *
     */
    public Boolean dealSmsResultStatus(List<SmsReqEntity.SmsMsgEntity> send_patch ,JSONObject http_rets){
        Jlog.info("deal sms result:"+http_rets.toJSONString());
        Boolean st = true;
        SmsRespEntity smsRespEntity = JSONObject.toJavaObject(http_rets,SmsRespEntity.class);
        String send_time = Jdate.getNowStrTime();
        try {

            String status = AppConstants.SMS_SUBMIT_CODE_STATUS.get(smsRespEntity.getResult());
            for(SmsReqEntity.SmsMsgEntity item: send_patch){
                tasksResultDao.updateNoticeTaskResultStatus( item.getMsgid(),
                        status,
                        "",
                        send_time);
            }
            if (smsRespEntity.getResult().equals("0") == true) {
                if (smsRespEntity.getData().size() > 0) {
                    List<SmsRespEntity.SmsRespDataEntity> items = smsRespEntity.getData();
                    for (SmsRespEntity.SmsRespDataEntity item : items) {
                        tasksResultDao.updateNoticeTaskResultStatus( item.getMsgid(),
                                AppConstants.SMS_REPORT_CODE_STATUS.get(item.getStatus()),
                                item.getDesc(),
                                send_time);
                    }

                }

            }
        }catch(Exception ee){
            Jlog.error(" update sms result error:"+ee.getMessage());
            st = false;
        }
        return st;
    }

    /**
     * 间隔获取发送短信的状态，更新本地数据库的状态,
     * @param rets
     * @return
     */
    public Boolean dealSmsResultReportStatus(JSONObject rets){
        Jlog.info("begin deal sms result report status:");
        SmsReportEntity smsReportEntity = JSONObject.toJavaObject(rets,SmsReportEntity.class);
        Boolean st = true;
        String back_time = Jdate.getNowStrTime();
        try{
            if(smsReportEntity.getReports() != null && smsReportEntity.getResult().equals("0") == true){
                if(smsReportEntity.getReports().size() > 0){
                    List<SmsReportEntity.SmsReportInfoEntity> items = smsReportEntity.getReports();
                    for(SmsReportEntity.SmsReportInfoEntity item:items){
                        tasksResultDao.updateNoticeTaskBackInfoStatus(item.getMsgid(),
                                item.getStatus(),item.getDesc(),item.getTime(),back_time);
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
