package com.spiderdt.common.notice;

import com.spiderdt.common.notice.common.Jlog;
import com.spiderdt.common.notice.service.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 *
 */
public class App 
{
    private static Logger log = LoggerFactory.getLogger(App.class);
    public static void main( String[] args )
    {
        ApplicationContext ctx= new ClassPathXmlApplicationContext("applicationContext.xml");
        SmsService smsService =(SmsService) ctx.getBean("smsService");
        Jlog.info("begin run notice service and get sms service");
        /*
        Jedis redis = Jredis.getJedis();
        String name =redis.get("qiong");
        JredisClient.addString("qiongye","good girl");
        JredisClient.addString("test1","value",100);
        JredisClient.setDataToRedis("test2","key1","vluae1");
        String dd = JredisClient.getString("qiongye");
        */
        //smsService.sendSmsByTaskId(1);
        while(true) {
            Jlog.info("begin send sms by db scan");
            smsService.sendSmsByDbScan();
            Jlog.info("end send sms by db scan and sleep....");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Jlog.error("sleep error:"+e.getMessage());
            }
        }
        //smsService.createSmsTask("c1","user1","18217168545","test");
    }
}
