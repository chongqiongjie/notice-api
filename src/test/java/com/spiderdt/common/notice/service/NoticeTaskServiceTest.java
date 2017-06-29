package com.spiderdt.common.notice.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import com.spiderdt.common.notice.common.AppConstants;
import com.spiderdt.common.notice.common.Jlog;
import com.spiderdt.common.notice.common.Sredis;
import com.spiderdt.common.notice.entity.NoticeTasksEntity;
import com.spiderdt.common.notice.errorhander.AppException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Set;

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

    @Autowired
    Sredis sredis;

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
        System.out.println(noticeTaskService.getAddressesFromJobId(job_id,"email"));
        System.out.println(noticeTaskService.getAddressesFromJobId(job_id,"sms"));
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

    @Test
    public void testCreateSmsTask() throws AppException {
        NoticeTasksEntity noticeTasksEntity = new NoticeTasksEntity();
        noticeTasksEntity.setTaskType("sms");
        noticeTasksEntity.setClientId("jupiter");
        noticeTasksEntity.setUserId("test");
        noticeTasksEntity.setJobId("job_test_id");
        noticeTasksEntity.setTemplateId(1);
        noticeTasksEntity.setMessage("#name# 您好，http://baidu.com 还有 http://z.cn ");
        //如果task_type==sms时
        //noticeTasksEntity.setSubject("#name# 专属");
        //noticeTasksEntity.setAttachments("http://xxxxxxx");
        noticeTaskService.createNoticeTask(noticeTasksEntity);
    }


    @Test
    public void countUnfixedResultByTaskId() {
        int count = noticeTaskService.countUnfixedResultByTaskId(108);
        System.out.println(noticeTaskService.countUnfixedResultByTaskId(108));
        if (count == 0) {
            Jlog.info("is last so delete task attachment dirctory!");
        }
//        System.out.println(noticeTaskService.countUnfixedResultByTaskId(106));
//        System.out.println(noticeTaskService.countUnfixedResultByTaskId(102));
    }

    @Test
    public void testupdateTaskStatusBatch() throws AppException {
        Set<Integer> task_ids = Sets.newHashSet();
        task_ids.add(147);
        task_ids.add(146);
        task_ids.add(145);
        noticeTaskService.updateTaskStatusBatch(task_ids, AppConstants.TASK_STATUS_SENDING);
    }

    @Test
    public void getAddressesFromJobId2() throws Exception {
        String job_id = "latetime_feature2_20161101_20161115";

        JSONArray ret = new JSONArray();

        String clientInfo = noticeTaskService.getAddressesFromJobId(job_id,"email");
        System.out.println(clientInfo);

        JSONObject clientInfoJson = (JSONObject) JSON.parse(clientInfo);
        System.out.println(clientInfoJson);
        System.out.println(clientInfoJson.get("client_info"));

        JSONArray emailAndPhoneArray = (JSONArray) clientInfoJson.get("client_info");
        System.out.println(emailAndPhoneArray);
        for (int i = 0; i < emailAndPhoneArray.size(); i++) {
            JSONArray emailAndPhoneArray2 = emailAndPhoneArray.getJSONArray(i);
            for (int j = 0; j < emailAndPhoneArray2.size(); j++) {
                JSONObject o = emailAndPhoneArray2.getJSONObject(j);
                System.out.println(o.get("info_list"));
//                System.out.println(o.get("info_list").getClass());
                String email = (String) o.get("e_mail");
                if(email != null) {
                    System.out.println(email);
                }

                JSONArray emailAndPhoneArray3 = JSON.parseArray((String) o.get("info_list"));
                emailAndPhoneArray3.remove(0);
                if (emailAndPhoneArray3.size() == 0) {
                    System.out.println("已经取完了");
                }
                for (int k = 0; k < emailAndPhoneArray3.size(); k++) {
//                    System.out.println(emailAndPhoneArray3.get(k));
//                    System.out.println(emailAndPhoneArray3.get(k).getClass());
                    JSONArray jsonArray = (JSONArray) emailAndPhoneArray3.get(k);
                    String name = (String) jsonArray.get(0);
                    String phone = (String) jsonArray.get(1);
                    System.out.println("name:" + name + " address:" + phone);
                }
            }
        }


    }

    @Test
    public void redisTest() {
//        String singleUserInfo = "[[\"张旭昊\",\"18936255608\",\"江苏省\",\"扬州市\",\"江都区\",1,0,0,1],[\"袁雨静\",\"15062867110\",\"江苏省\",\"扬州市\",\"江都区\",1,0,0,1]]";
//        String singleUserInfo = "[[\"张旭昊\",\"18936255608\",\"江苏省\",\"扬州市\",\"江都区\",1,0,0,1]]";
        String singleUserInfo = "[]";
        String riid = "keyRiid";
        sredis.addString(riid, singleUserInfo);
//        sredis.addString(riid, "testValue");

        singleUserInfo = sredis.getString(riid);
        System.out.println(singleUserInfo);

        if ("[]".equals(singleUserInfo)) {
            System.out.println("取完了");
            return;
        }

        JSONArray singleUserInfoArray = JSON.parseArray(singleUserInfo);
        System.out.println(singleUserInfoArray);
        JSONArray jsonArray = (JSONArray) singleUserInfoArray.get(0);
        String name = (String) jsonArray.get(0);
        String phone = (String) jsonArray.get(1);
        singleUserInfoArray.remove(0);
        singleUserInfo = singleUserInfoArray.toJSONString();
        sredis.addString(riid, singleUserInfo);
        singleUserInfo = sredis.getString(riid);
        System.out.println(singleUserInfo);
    }


    @Test
    public void redisTest2() {
        System.out.println(sredis.getString("0f5985d69fb94ae4a60c70dd790c7f66")); // null
        System.out.println(sredis.getString("bdad48caa3dc479ebeefece8f4b7eb06")); // [["test晨","13458555648","安徽省","宣城市","郎溪县",1,0,0,1]]
        System.out.println(sredis.getString("679ea0601ee3426a8be61a4b383f4d4e"));
    }
}