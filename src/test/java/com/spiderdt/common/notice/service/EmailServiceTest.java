package com.spiderdt.common.notice.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.spiderdt.common.notice.entity.NoticeTasksResultEntity;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ranran
 * @version V1.0
 * @Title:
 * @Package com.spiderdt.common.notice.service
 * @Description:
 * @date 2017/6/6 15:17
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class EmailServiceTest extends TestCase {

    @Autowired
    private EmailService emailService;


    @Test
    public void sendEmailBatch() {
        List<NoticeTasksResultEntity> items = new ArrayList<>();
        NoticeTasksResultEntity item = new NoticeTasksResultEntity();
        item.setAddress("ran.bo@spiderdt.com");
        item.setMessage("test 您好 <!DOCTYPE html>\n" +
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
                "}\n" +
                "<a style=\"color: #337ab7; text-decoration: none;\" href=\"http://127.0.0.1:8080/track/8BB2E5DC6B7AA17773D24E130102D86Bd?ticket=b9387ba1606a5537e18acef496f033c516551b8a\" target=\"_blank\">点击此处更改密码</a>}\n" +
                "<a  href=\"http://127.0.0.1:8080/track/8BB2E5DC6B7AA17773D24E130102D86B\" target=\"_blank\">点击此处更改密码</a></style>\n" +
                "<a href=\"http://127.0.0.1:8080/track/1046E46456207935E9FFFC2DB8CE0DE0\">3</a></html><img src=\"http://127.0.0.1:8080/track/2CAC3CB01D27CD03A63B856DE49CA6BA\" width=\"0\" height=\"0\" style=\"display: none\"/>");
        item.setRiid("47b583823b274267be0cc874f0d6697c");
        item.setSubject("Test Email");
        item.setTaskId(33);
        items.add(item);
        emailService.sendEmailBatch(items);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void TestString2Json() {
        String testString = "[{\"fileName\":\"test.jpg\", \"downloadUrl\":\"http://www.qqleju.com/uploads/allimg/130618/18-110930_9.jpg\"}, {\"fileName\":\"test2.jpg\", \"downloadUrl\":\"http://www.qqleju.com/uploads/allimg/130618/18-110930_9.jpg\"}]";
        JSONArray jsonArray = JSON.parseArray(testString);
        for (Object jsonObject : jsonArray) {
            Map<String, String> attachmentAttributeMap = (Map<String, String>) jsonObject;
            System.out.println(attachmentAttributeMap);
            System.out.println(attachmentAttributeMap.get("downloadUrl"));
            System.out.println(attachmentAttributeMap.get("fileName"));

        }
    }

}