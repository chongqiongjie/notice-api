package com.spiderdt.common.notice.common;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ranran
 * @version V1.0
 * @Title:
 * @Package com.spiderdt.common.notice.common
 * @Description:
 * @date 2017/6/16 11:19
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class UtilsTest {

    @Autowired
    Utils utils;

    @Test
    public void checkTokenFormat() throws Exception {
    }

    @Test
    public void formatExpiredTime() throws Exception {
    }

    @Test
    public void checkAuthBearerHeader() throws Exception {
    }

    @Test
    public void checkAuthBasicaHeader() throws Exception {
    }

    @Test
    public void encodePassword() throws Exception {
    }

    @Test
    public void getNewToken() throws Exception {
    }

    @Test
    public void checkPasswordFormat() throws Exception {
    }

    @Test
    public void getRespons() throws Exception {
    }

    @Test
    public void getRespons1() throws Exception {
    }

    @Test
    public void getRespons2() throws Exception {
    }

    @Test
    public void getRespons3() throws Exception {
    }

    @Test
    public void checkString() throws Exception {
    }

    @Test
    public void replaceString() throws Exception {
    }

    @Test
    public void mergMap() throws Exception {
    }

    @Test
    public void mergMap1() throws Exception {
    }

    @Test
    public void map2Json() throws Exception {
    }

    @Test
    public void json2map() throws Exception {
    }

    @Test
    public void getUrlFromMessage() throws Exception {
    }

    @Test
    public void getUrlsFromMessage() throws Exception {

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
                "<img src=\"http://localhost:8080/track/open123\" width=\"0\" height=\"0\" style=\"display: none\">\n"+
                "</body>\n" +
                "<style>\n" +
                "a:hover {\n" +
                "color: #5b9ed8 !important;\n" +
                "text-decoration: underline !important;\n" +
                "}\n" + "<a style=\"color: #337ab7; text-decoration: none;\" href=\"https://cocacola.spiderdt.com/reset-password?ticket=b9387ba1606a5537e18acef496f033c516551b8a\" target=\"_blank\">点击此处更改密码</a>" +
                "}\n" + "<a  href=\"https://cocacola.spiderdt.com/reset-passwor\" target=\"_blank\">点击此处更改密码</a>" +
                "</style>\n" +
                "<a href=\"https://www.google.com#/bai\">3</a>" +
                "<img src=\"http://blog.csdn.net/yaerfeng/article/details/18402569\">\n"+
                "</html>";
//        html = "this <a href='http://www.baidu.com'>3</a>is email message";
        String taskType = "email";
        HashMap<Integer, String> urls = Utils.getUrlsFromMessage(html, taskType);
        for (Map.Entry<Integer, String> entry : urls.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = "  + entry.getValue());
        }

    }

    @Test
    public void replaceUrlFromMessage() throws Exception {
    }

    @Test
    public void replaceUrlFromMessage1() throws Exception {
    }

    @Test
    public void replaceAllALinks() throws Exception {
    }

    @Test
    public void getUUID() throws Exception {
    }

}