package com.spiderdt.common.notice.service;

import com.spiderdt.common.notice.common.Jlog;
import com.spiderdt.common.notice.entity.NoticeTasksResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ranran on 2017/6/6.
 */
@SuppressWarnings("ALL")
@Service("emailService")
public class EmailService {

    @Autowired
    private JavaMailSenderImpl sender;

    /**
     * 发送文本邮件
     * @param desEmailAddr
     * @param subject
     * @param content
     */
    public void sendText (String desEmailAddr, String subject, String content) {
        MimeMessage mimeMsg = sender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(mimeMsg, true, "utf-8");
            helper.setText(content, false);
            helper.setTo(desEmailAddr);
            helper.setSubject(subject);
            helper.setFrom(sender.getUsername());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        Jlog.info("------------ sendTextEmail");
        sender.send(mimeMsg);
    }

    /**
     * 发送 Html 邮件
     * @param desEmailAddr
     * @param subject
     * @param content
     */
    public void sendHtml(String desEmailAddr, String subject, String content) {
        MimeMessage mimeMsg = sender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(mimeMsg, true, "utf-8");
            helper.setText(content,true);
            helper.setTo(desEmailAddr);
            helper.setSubject(subject);
            helper.setFrom(sender.getUsername());
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        Jlog.info("------------ sendHtmlEmail");
        sender.send(mimeMsg);
    }

    /**
     * 下载文件到本地
     * @param urlString 被下载的文件地址
     * @param filename 本地文件名
     */

    public  void download(String urlString, String filename)
    {

        try
        {
            URL url = new URL(urlString);
            // 打开连接
            URLConnection con = url.openConnection();
            // 输入流
            InputStream is = con.getInputStream();
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流s
            OutputStream os = new FileOutputStream("/Users/qiong/work/spiderdt/file/" + filename);
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            // 完毕，关闭所有链接
            os.close();
            is.close();
        }
        catch (MalformedURLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



    /**
     * 发送附件邮件
     * @param desEmailAddr
     * @param subject
     * @param content
     * @param path 附件路径
     */
    public void sendAttachment(String desEmailAddr, String subject, String content, String path) {
        MimeMessage mimeMsg = sender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(mimeMsg, true, "utf-8");
            helper.setText(content, true);
            // 添加附件
            FileSystemResource file = new FileSystemResource(new File(path));
            //用于解决邮件显示附件名中含有中文
            ClassPathResource fileName = new ClassPathResource(path);
            try {
                helper.addAttachment(MimeUtility.encodeWord(fileName.getFilename()), file);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            helper.setTo(desEmailAddr);
            helper.setSubject(subject);
            helper.setFrom(sender.getUsername());
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        Jlog.info("------------ sendAttachmentEmail");
        sender.send(mimeMsg);
    }


    /**
     * 发送多个附件邮件
     * @param desEmailAddr
     * @param subject
     * @param content
     * @param path 附件路径
     */
    public void sendAttachments(String desEmailAddr, String subject, String content,List<String> paths) {
        MimeMessage mimeMsg = sender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(mimeMsg, true, "utf-8");
            helper.setText(content, true);
            // 添加附件
            for (String path:paths) {
                FileSystemResource file = new FileSystemResource(new File(path));
                //用于解决邮件显示附件名中含有中文
                ClassPathResource fileName = new ClassPathResource(path);
                try {
                    helper.addAttachment(MimeUtility.encodeWord(fileName.getFilename()), file);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            helper.setTo(desEmailAddr);
            helper.setSubject(subject);
            helper.setFrom(sender.getUsername());
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        Jlog.info("------------ sendAttachmentEmail");
        sender.send(mimeMsg);
    }

    /**
     * 发送一张图片邮件
     * @param desEmailAddr
     * @param subject
     * @param content
     * @param imgId 在 content 中必须有 <img src="cid:%s"/>
     * @param path 图片路径
     */
    public void sendPhoto(String desEmailAddr, String subject, String content, String imgId, String path) {
        MimeMessage mimeMsg = sender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(mimeMsg, true, "utf-8");
            // format 用于加入图片位置
            content = String.format(content, imgId);
            helper.setText(content, true);
            // 添加内嵌图片
            FileSystemResource img = new FileSystemResource(new File(path));
            helper.addInline(imgId, img);
            helper.setTo(desEmailAddr);
            helper.setSubject(subject);
            helper.setFrom(sender.getUsername());
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        Jlog.info("------------ sendPhotoEmail");
        sender.send(mimeMsg);
    }

    public void sendPhoto(String desEmailAddr, String subject, String content, HashMap<Integer, String> imgIdMap, HashMap<Integer, String> imgPathMap) {
        MimeMessage mimeMsg = sender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(mimeMsg, true, "utf-8");
            for(HashMap.Entry<Integer, String> entry: imgIdMap.entrySet()) {
                int order = entry.getKey();
                String imgId = entry.getValue();
                String imgPath = imgPathMap.get(order);
                // format 用于加入图片位置
                content = String.format(content, imgId);
                helper.setText(content, true);
                // 添加内嵌图片
                FileSystemResource img = new FileSystemResource(new File(imgPath));
                helper.addInline(imgId, img);
            }

            helper.setTo(desEmailAddr);
            helper.setSubject(subject);
            helper.setFrom(sender.getUsername());
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        Jlog.info("------------ sendPhotoEmail");
        sender.send(mimeMsg);
    }

    /**
     * 批量发送邮件
     * @param items
     * @return
     */
    public Boolean sendEmailBatch(List<NoticeTasksResultEntity> items){

        return true;
    }

}
