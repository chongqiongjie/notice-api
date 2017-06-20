package com.spiderdt.common.notice.service;

import com.spiderdt.common.notice.common.Jdate;
import com.spiderdt.common.notice.common.Jlog;
import com.spiderdt.common.notice.dao.TasksResultDao;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ranran on 2017/6/6.
 */
@SuppressWarnings("ALL")
@Service("emailService")
public class EmailService {

    @Autowired
    private JavaMailSenderImpl sender;

    @Autowired
    private TasksResultDao tasksResultDao;

    @Autowired
    private NoticeTaskService noticeTaskService;

    /**
     * 设置发送邮件的基本信息, 包括地址，主题，内容
     * @param desEmailAddr
     * @param subject
     * @param content
     * @param helper
     */
    public void setEmailBasicInfo(String desEmailAddr, String subject, String content, MimeMessageHelper helper) {
        try {
            helper.setText(content,true);
            helper.setTo(desEmailAddr);
            helper.setSubject(subject);
            helper.setFrom(sender.getUsername());
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        Jlog.info("------------ add desEmailAddr subject content");
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
     * 设置发送多个附件的邮件
     * @param paths 多个附件 path 的集合
     * @param helper
     */
    public void setAttachments(ArrayList<String> paths, MimeMessageHelper helper) {
        try {
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
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        Jlog.info("------------ add attachments");
    }


    /**
     * 批量发送邮件, 并且更新 notice_tasks_result_info 表 和 notice_tacks 表
     * @param items
     * @return
     */
    public Boolean sendEmailBatch(List<NoticeTasksResultEntity> items){
        String sendTime = "";
        String backTime = "";
        int taskId;
        String detailInfo;
        String sendStatus;

        for (NoticeTasksResultEntity item: items) {
            taskId = item.getTaskId();
            noticeTaskService.updateNoticeTaskSatus(taskId, "sending");
            try {
                sendTime = Jdate.getNowStrTime();
                MimeMessage mimeMsg = sender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMsg, true, "utf-8");

                setEmailBasicInfo(item.getAddress(), item.getSubject(), item.getMessage(), helper);

                ArrayList<String> paths = new ArrayList<>();
                // TODO 将前端的给的附件下载存入磁盘，然后　paths 为本地的附件路径　
                paths = noticeTaskService.getAttachmentByTaskId(taskId);
                // path list 中可能有一个值，但是为 ""
                if (!"".equals(paths.get(0))) {
                    setAttachments(paths, helper);
                }

                sender.send(mimeMsg);
                detailInfo = "success";
                sendStatus = "success";
            } catch (Exception ee) {
                Jlog.error("send emailAddress:"+item.getAddress());
                Jlog.error("send email error:"+ee.getMessage());
                detailInfo = ee.getMessage();
                sendStatus = "failed";
            }
            noticeTaskService.updateNoticeTaskSatus(taskId, "sended");

            backTime = Jdate.getNowStrTime();
            Jlog.info("update notice_tacks_result_info backTime status riid :" + item.getRiid());
            tasksResultDao.updateNoticeTaskBackInfoStatus(item.getRiid(), sendStatus, detailInfo, sendTime, backTime);
        }
        return true;
    }

}
