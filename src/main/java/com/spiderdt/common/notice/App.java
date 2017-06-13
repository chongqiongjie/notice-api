package com.spiderdt.common.notice;

import com.spiderdt.common.notice.common.Jlog;
import com.spiderdt.common.notice.service.NoticeTaskService;
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
        NoticeTaskService noticeTaskService =(NoticeTaskService)ctx.getBean("noticeTaskService");
        Jlog.info("begin run notice service and get notice task service");
        while(true) {
            Jlog.info("loop begin send by db scan");
            try {
                noticeTaskService.sendNoticeTaskByDbScan();
            }catch (Exception ee){
                Jlog.error("send notice by db scan task error:"+ee.getMessage());
            }
            Jlog.info("loop end send notice by db scan and sleep....");
            try {
                Thread.sleep(10000);     //sleep
            } catch (InterruptedException e) {
                Jlog.error("sleep error:"+e.getMessage());
            }
            Jlog.requestid = "";
        }
    }
}
