package com.spiderdt.common.notice.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.spiderdt.common.notice.entity.NoticeTasksEntity;
import com.spiderdt.common.notice.errorhander.AppException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author ranran
 * @version V1.0
 * @Title:
 * @Package com.spiderdt.common.notice.service
 * @Description:
 * @date 2017/6/16 12:37
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class NoticeTaskServiceTest {

    @Autowired
    NoticeTaskService noticeTaskService;

    @Test
    public void sendNoticeTaskByDbScan() throws Exception {

        noticeTaskService.sendNoticeTaskByDbScan();

    }

    @Test
    public void createNoticeTask() throws Exception {
        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "<meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\n" +
                "<title>Document</title>\n" +
                "</head>\n" +
                "<body style=\"color: #666\">\n" +
                "<p><strong>您好:</strong></p >\n" +
                "<img src=\"http://localhost:8080/track/open123\" width=\"0\" height=\"0\" style=\"display: none\">\n" +
                "</body>\n" +
                "<style>\n" +
                "a:hover {\n" +
                "color: #5b9ed8 !important;\n" +
                "text-decoration: underline !important;\n" +
                "}\n" + "<a style=\"color: #337ab7; text-decoration: none;\" href=\"https://cocacola.spiderdt.com/reset-password?ticket=b9387ba1606a5537e18acef496f033c516551b8a\" target=\"_blank\">点击此处更改密码</a>" +
                "}\n" + "<a  href=\"https://cocacola.spiderdt.com/reset-passwor\" target=\"_blank\">点击此处更改密码</a>" +
                "</style>\n" +
                "<a href=\"https://www.google.com#/bai\">3</a>" +
                "</html>";
        NoticeTasksEntity noticeTasksEntity = new NoticeTasksEntity();
        noticeTasksEntity.setTaskType("email");
        noticeTasksEntity.setClientId("jupiter");
        noticeTasksEntity.setUserId("test");
        noticeTasksEntity.setJobId("test_Jb");
        noticeTasksEntity.setTemplateId(1);
        noticeTasksEntity.setMessage("#name# 您好 " + html);
        //如果task_type==sms时
        noticeTasksEntity.setSubject("#name# 专属");
        noticeTasksEntity.setAttachments("[{\"fileName\":\"test.jpg\", \"downloadUrl\":\"http://www.qqleju.com/uploads/allimg/130618/18-110930_9.jpg\"}, {\"fileName\":\"test2.jpg\", \"downloadUrl\":\"http://www.qqleju.com/uploads/allimg/130618/18-110930_9.jpg\"}]");

        Boolean st = noticeTaskService.createNoticeTask(noticeTasksEntity);
        System.out.println(st);

    }

    @Test
    public void checkNoticeTaskParams() throws Exception {
    }

    @Test
    public void getAddressesFromJobId() throws Exception {
       String job_id = "latetime_feature2_20161101_20161115";
        System.out.println(noticeTaskService.getAddressesFromJobId(job_id));
    }

    @Test
    public void createTaskResultInfo() throws Exception {
    }

    @Test
    public void getAddresssesFromJsonString() throws Exception {
    }

    @Test
    public void makeTrackRecodeInfo() throws Exception {
    }

    @Test
    public void replaceMessageUrlToTrackUrl() throws Exception {
    }

    @Test
    public void replaceUrlFromMessage() throws Exception {
    }

    @Test
    public void addOpenTrackUrl() throws Exception {
    }

    @Test
    public void makeTaskResultInfo() throws Exception {
    }

    @Test
    public void saveNoticeResultsBatch() throws Exception {
    }

    @Test
    public void saveTrackRecodeBatch() throws Exception {
    }

    @Test
    public void getAttachmentByTaskId() {
        System.out.println(noticeTaskService.getAttachmentByTaskId(45));
//        System.out.println(noticeTaskService.getAttachmentByTaskId(31));
//        System.out.println(noticeTaskService.getAttachmentByTaskId(32));
//        System.out.println(noticeTaskService.getAttachmentByTaskId(33));
    }



}