package com.spiderdt.common.notice.task;

import com.alibaba.fastjson.JSONObject;
import com.spiderdt.common.notice.common.AppConstants;
import com.spiderdt.common.notice.common.JhttpClient;
import com.spiderdt.common.notice.common.Jlog;
import com.spiderdt.common.notice.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by fivebit on 2017/5/23.
 * 例行化的脚本，每隔30秒，去获取一次短信状态。更新数据库状态。
 * 该短信状态，只会返回一次，且每次只会返回不超过200条。
 */
public class ScheduleSyncSmsSendStatusTask implements Runnable {
    @Autowired
    private SmsService smsService;

    private JSONObject header = null;
    private String sms_host_url = "http://www.dh3t.com/json/sms/Report";

    public  ScheduleSyncSmsSendStatusTask(){
        header = new JSONObject();
        header.put("account", AppConstants.SMS_ACCOUNT);
        header.put("password",AppConstants.SMS_PASSWORD);

    }

    @Override
    public void run() {
        Jlog.info("schedule sms sync status task begin;");
        JSONObject rets = JhttpClient.httpPost(sms_host_url,header);
        smsService.dealSmsResultReportStatus(rets);
        Jlog.info("http post return:"+rets.toJSONString());
    }
}
