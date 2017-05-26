package com.spiderdt.common.notice.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.spiderdt.common.notice.common.JhttpClient;
import com.spiderdt.common.notice.common.Jlog;
import com.spiderdt.common.notice.entity.SmsReqEntity;
import com.spiderdt.common.notice.service.SmsService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * Created by fivebit on 2017/5/23.
 */
public class DefaultSmsSendTask implements  SmsRunTask{
    private List<SmsReqEntity.SmsMsgEntity> smsMsgEntitys;
    private Integer task_id;
    private Integer sms_batch = 10;     //每批发送10条短信
    private static ApplicationContext ctx = null;
    private static String sms_host_url = "http://www.dh3t.com/json/sms/BatchSubmit";
    private String account = "";
    private String password = "";
    public DefaultSmsSendTask(Integer task_id){
        if(ctx == null){
            ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        }
        this.task_id = task_id;
    }
    @Override
    public void setMsg(Object msg) {
        smsMsgEntitys = (List<SmsReqEntity.SmsMsgEntity>) msg;
    }

    @Override
    public Object clone() {
        Object clone_object = null;
        try{
            clone_object = super.clone();
            Jlog.info("clone object success");
        }catch(CloneNotSupportedException ee) {
        }
        return clone_object;
    }

    /**
     * 切包，每次最多500个手机号。
     * 我们这边设置成sms_batch一组
     */
    @Override
    public void run() {
        Jlog.info("start  send sms task :"+task_id);
        SmsService smsService = (SmsService) ctx.getBean("smsService");
        smsService.updateNoticeTaskSatus(task_id,"sending");
        SmsReqEntity sendEntity = new SmsReqEntity();
        sendEntity.setPassword(password);
        sendEntity.setAccout(account);
        if(smsMsgEntitys.size() == 0){
            Jlog.error("send sms task entity empty:"+task_id);
            return;
        }
        int list_size = smsMsgEntitys.size();
        float fsize = (float) sms_batch;
        int count = (int) Math.ceil(list_size/fsize);
        try {
            for (int i = 0; i < count; i++) {
                int end = (i + 1) * sms_batch;
                if (end > list_size) {
                    end = list_size;
                }
                List<SmsReqEntity.SmsMsgEntity> patch = smsMsgEntitys.subList(i * sms_batch, end);
                sendEntity.setData(patch);
                JSONObject ret = JhttpClient.httpPost(sms_host_url,(JSONObject) JSON.toJSON(sendEntity));
                Jlog.info("http post return:"+ret.toJSONString());
                smsService.dealSmsResultStatus(task_id,ret);
            }
        }catch (Exception ee){
            Jlog.error("send notice error:"+ee.getMessage());
        }

        smsService.updateNoticeTaskSatus(task_id,"sended");
        Jlog.info("end send sms task :"+task_id);
    }
}
