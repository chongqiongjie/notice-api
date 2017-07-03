package com.spiderdt.common.notice.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.spiderdt.common.notice.common.AppConstants;
import com.spiderdt.common.notice.common.Jdate;
import com.spiderdt.common.notice.common.Jlog;
import com.spiderdt.common.notice.common.Sredis;
import com.spiderdt.common.notice.dao.NoticeTasksDao;
import com.spiderdt.common.notice.dao.TasksResultDao;
import com.spiderdt.common.notice.dao.TrackRecodeDao;
import com.spiderdt.common.notice.entity.*;
import com.spiderdt.common.notice.errorhander.AppException;
import com.spiderdt.common.notice.task.DefaultSmsSendTask;
import com.spiderdt.common.notice.task.SmsRunTask;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private Sredis sredis;

    @Autowired
    private NoticeTaskService noticeTaskService;

    @Autowired
    private NoticeTaskResultErrorService noticeTaskResultErrorService;

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
        Jlog.info("call sms service begin");
        SmsRunTask smsRunTask = new DefaultSmsSendTask();
        smsRunTask.setMsg(smsMsgEntitys);
        taskPool.execute(smsRunTask);
        Jlog.info("call sms service end");
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
        Boolean st = true;
        SmsRespEntity smsRespEntity = JSONObject.toJavaObject(http_rets,SmsRespEntity.class);
        Jlog.debug("deal sms result:"+http_rets.toJSONString() +" and:"+smsRespEntity.toString());
        String submitTime = Jdate.getNowStrTime();
        try {
            String detailInfo =  AppConstants.SMS_SUBMIT_CODE_STATUS.get(Integer.valueOf(smsRespEntity.getResult()));
            if (smsRespEntity.getResult().equals("0") == true) {
                if (smsRespEntity.getData().size() > 0) {
                    List<SmsRespEntity.SmsRespDataEntity> items = smsRespEntity.getData();
                    for (SmsRespEntity.SmsRespDataEntity item : items) {
                        Jlog.info("dealSmsResultStatus error item:" + item.getMsgid());

                        String riid = item.getMsgid();
                        // taskId 初始给 0
                        int taskId = 0;
                        detailInfo = AppConstants.SMS_REPORT_CODE_STATUS.get(Integer.valueOf(item.getStatus()));

                        NoticeTasksResultErrorEntity noticeTasksResultErrorEntity = new NoticeTasksResultErrorEntity(taskId,
                                item.getFailPhones(), AppConstants.TASK_RESULT_STATUS_FAILED,detailInfo,submitTime);
                        noticeTaskResultErrorService.insertOneIntoResultError(riid, noticeTasksResultErrorEntity);

                        // 如果缓存中还有就,用下一个手机号发短信,没有就是失败状态
                        String singleUserInfo = sredis.getString(riid);
                        if((singleUserInfo == null) || "[]".equals(singleUserInfo)) {
                            tasksResultDao.updateNoticeTaskResultStatus( item.getMsgid(),
                                    AppConstants.TASK_RESULT_STATUS_FAILED, detailInfo, submitTime);
                        } else {
                            updateNextClientInfo(riid, singleUserInfo);
                        }

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
        Jlog.info("begin deal sms result report status: rets:" + rets);
        SmsReportEntity smsReportEntity = JSONObject.toJavaObject(rets,SmsReportEntity.class);
        Boolean st = true;
        String backTime = Jdate.getNowStrTime();
        try{
            if(smsReportEntity.getReports() != null && smsReportEntity.getResult().equals("0") == true){
                Jlog.info("begin deal sms result report status ******************1");
                if(smsReportEntity.getReports().size() > 0){
                    List<SmsReportEntity.SmsReportInfoEntity> items = smsReportEntity.getReports();
                    for(SmsReportEntity.SmsReportInfoEntity item:items){
                        String status = AppConstants.TASK_RESULT_STATUS_SUCCESS;
                        String detailInfo = AppConstants.SMS_REPORT_CODE_STATUS.get(Integer.valueOf(item.getStatus()));
                        if(item.getStatus().equals("0") == false){
                            String riid = item.getMsgid();
                            // taskId 初始给 0
                            int taskId = 0;

                            NoticeTasksResultErrorEntity noticeTasksResultErrorEntity = new NoticeTasksResultErrorEntity(taskId,
                                    item.getPhone(), AppConstants.TASK_RESULT_STATUS_FAILED,detailInfo,backTime);
                            noticeTaskResultErrorService.insertOneIntoResultError(riid, noticeTasksResultErrorEntity);

                            Jlog.info("dealSmsResultReportStatus riid:" + riid);
                            // 如果缓存中还有就,用下一个手机号发短信,没有就是失败状态
                            String singleUserInfo = sredis.getString(riid);
                            if((singleUserInfo == null) || "[]".equals(singleUserInfo)) {
                                status = AppConstants.TASK_RESULT_STATUS_FAILED;
                                tasksResultDao.updateNoticeTaskBackInfoStatus(item.getMsgid(), status, detailInfo,item.getTime(),backTime);
                            } else {
                                updateNextClientInfo(riid, singleUserInfo);
                            }

                        } else {
                            tasksResultDao.updateNoticeTaskBackInfoStatus(item.getMsgid(), status,detailInfo,item.getTime(),backTime);
                        }

                    }
                }
            }
        }catch(Exception ee){
            Jlog.error("deal sms result report error:"+ee.getMessage());
            st = false;
        }
        return st;
    }

    /**
     * 使用下一个客户的电话号码进行更新
     * @param riid
     * @param singleUserInfo 单个客户的剩余号码信息
     */
    public void updateNextClientInfo(String riid, String singleUserInfo) {

        JSONArray singleUserInfoArray = JSON.parseArray(singleUserInfo);
        Jlog.info("手机号错误,使用剩下的手机号的第一个进行发送,剩下的手机号为:" + singleUserInfoArray);
        JSONArray jsonArray = (JSONArray) singleUserInfoArray.get(0);
        String phone = (String) jsonArray.get(1);
        singleUserInfoArray.remove(0);
        singleUserInfo = singleUserInfoArray.toJSONString();
        sredis.addString(riid, singleUserInfo);
        noticeTaskService.updateResultWithNextClientInfo(phone, riid);

    }

}
