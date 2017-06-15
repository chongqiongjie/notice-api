package com.spiderdt.common.notice.service;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    public void testSendHtmlEmail() throws Exception {
        System.out.println("------------------");
        System.out.println(emailService);
        System.out.println("******************");
//        emailService.sendHtml("ran.bo@spiderdt.com", "htmlTest","<html><head></head><body><h1>hello! Html test</h1></body></html>");

    }

    @Test
    public void testSendText() throws Exception {
//        emailService.sendText("ran.bo@spiderdt.com", "TextTest","this is a test!");
    }

    @Test
    public void testSendAttachment() throws Exception {
//        emailService.sendAttachment("ran.bo@spiderdt.com", "TextTest","this is a test!", "D:\\图片\\loveyue-master\\第一个.zip");
    }

    @Test
    public void testSendPhoto() throws Exception {
        // %s 用于 format 加入图片位置
        String content = "<html><head></head><body><h1>hello!!spring image html mail</h1><h2>大家好，这是Spring的邮件发送模块</h2>"
                + "<img src=\"cid:%s\"/></body></html>";
//        emailService.sendPhoto("ran.bo@spiderdt.com", "TextTest",content, "imgId","D:/图片/loveyue-master/月儿.png");
    }

    @Test
    public void testSendMultiplePhotos() throws Exception {
        // %s 用于 format 加入图片位置
        String content = "<html><head></head><body><h1>hello!!spring image html mail</h1><h2>大家好，这是Spring的邮件发送模块</h2>"
                + "<img src=\"cid:%1$s\"/></body></html>"+ "第二张图片" + "<img src=\"cid:%2$s\"/></body></html>";
        HashMap<Integer, String> imgPosition = new HashMap<Integer, String>();
        HashMap<Integer, String> imgPath = new HashMap<Integer, String>();
        imgPosition.put(1, "imgId1");
        imgPosition.put(2, "imgId2");
        imgPath.put(1, "D:/图片/loveyue-master/月儿.png");
        imgPath.put(2, "D:\\图片\\Camera Roll\\14803106.jpg");
//        emailService.sendPhoto("ran.bo@spiderdt.com", "TextTest", content, imgPosition, imgPath);
    }

    @Test
   public void sendAttachments(){
        String content = "<html><head></head><body><h1>hello!!spring image html mail</h1><h2>大家好，这是Spring的邮件发送模块</h2>";
        List<String> list = new ArrayList<>();
        list.add("/Users/qiong/Desktop/category.csv");
        list.add("/Users/qiong/Desktop/test");
//        emailService.sendAttachments("chong.qiongjie@spiderdt.com","TextTest",content,list);
    }

    @Test
    public void download(){
       emailService.download("http://www.qqleju.com/uploads/allimg/130618/18-110930_9.jpg", "chong");
    }


}