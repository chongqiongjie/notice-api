package com.spiderdt.common.notice.task;

import com.alibaba.fastjson.JSONObject;
import com.spiderdt.common.notice.common.JhttpClient;
import com.spiderdt.common.notice.common.Jlog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Created by fivebit on 2017/5/23.
 * 例行化的脚本，每隔30秒，去获取一次短信状态。更新数据库状态。
 * 该短信状态，只会返回一次，且每次只会返回不超过200条。
 */
public class ScheduleSmsTask implements Runnable {
    @Autowired
    private ThreadPoolTaskExecutor threadPool;
    private static ApplicationContext ctx = null;
    private JSONObject header = null;
    private String sms_host_url = "http://www.dh3t.com/json/sms/Report";
    public ScheduleSmsTask(ThreadPoolTaskExecutor taskExecutor) {
        this.threadPool= taskExecutor;
    }
    public  ScheduleSmsTask(){
        if(ctx == null){
            //ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        }
        header = new JSONObject();
        header.put("account","");
        header.put("password","");

    }

    @Override
    public void run() {
        Jlog.info("schedule sms task begin;");
        JSONObject rets = JhttpClient.httpPost(sms_host_url,header);

        //Jlog.info("http post return:"+rets.toJSONString());
    }
}
