package com.spiderdt.common.notice;

import com.spiderdt.common.notice.common.Jlog;
import com.spiderdt.common.notice.service.SmsService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * deamon 任务
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        ApplicationContext ctx= new ClassPathXmlApplicationContext("applicationContext.xml");
        SmsService smsService =(SmsService)ctx.getBean("smsService");
        Jlog.info("begin run notice service and get sms service");
        while(true) {
            Jlog.info("begin send sms by db scan");
            try {
                smsService.sendSmsByDbScan();
            }catch (Exception ee){
                Jlog.error("send sms by db scan task error:"+ee.getMessage());
            }
            Jlog.info("end send sms by db scan and sleep....");
            try {
                Thread.sleep(5000);     //sleep
            } catch (InterruptedException e) {
                Jlog.error("sleep error:"+e.getMessage());
            }
            Jlog.requestid = "";
        }
    }
}
