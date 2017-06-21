package com.spiderdt.common.notice.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.spiderdt.common.notice.common.Jdate;
import com.spiderdt.common.notice.common.Jlog;
import com.spiderdt.common.notice.dao.TasksResultDao;
import com.spiderdt.common.notice.entity.NoticeTasksEntity;
import com.spiderdt.common.notice.entity.NoticeTasksResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @Autowired
    FileService fileService;

    @Value("${attachment.storePath}") String attachmentStorePath;

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
     * 设置发送多个附件的邮件
     * @param paths 多个附件 path 的集合
     * @param helper
     */
    public void setAttachments(ArrayList<String> paths, MimeMessageHelper helper) {
        try {
            // 添加附件
            for (String path:paths) {
                File file = new File(path);
                if(!file.exists()) {
                    // 睡 10s 等待下载文件
                    Thread.sleep(10000);
                }
                FileSystemResource fileSystemResource = new FileSystemResource(file);
                //用于解决邮件显示附件名中含有中文
                ClassPathResource fileName = new ClassPathResource(path);
                try {
                    helper.addAttachment(MimeUtility.encodeWord(fileName.getFilename()), fileSystemResource);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
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

                NoticeTasksEntity tasksEntity = noticeTaskService.getAttachmentByTaskId(taskId);
                String attachmentJsonString = tasksEntity.getAttachments();
                String taskFileDir = attachmentStorePath + "/" + taskId;
                // path list 中可能有一个值，但是为 ""
                if (!"".equals(attachmentJsonString)) {
                    ArrayList<String> paths = new ArrayList<>();

                    JSONArray jsonArray = JSON.parseArray(attachmentJsonString);
                    for (Object jsonObject : jsonArray) {
                        Map<String, String> attachmentAttributeMap = (Map<String, String>) jsonObject;
                        String downloadUrl = attachmentAttributeMap.get("downloadUrl");
                        String fileName = attachmentAttributeMap.get("fileName");
                        String singleFilePath =  taskFileDir + "/" + fileName;
                        paths.add(singleFilePath);
                        Jlog.info(attachmentAttributeMap);
                    }
                    setAttachments(paths, helper);
                }
                sender.send(mimeMsg);
                if(item.isLast()){
                    fileService.deleteDirectory(taskFileDir);
                    Jlog.info("is last so delete task attachment dirctory!");
                    noticeTaskService.updateNoticeTaskSatus(taskId, "sended");
                }
                detailInfo = "success";
                sendStatus = "success";
            } catch (Exception ee) {
                Jlog.error("send emailAddress:"+item.getAddress());
                Jlog.error("send email error:"+ee.getMessage());
                detailInfo = ee.getMessage();
                sendStatus = "failed";
            }
            backTime = Jdate.getNowStrTime();
            Jlog.info("update notice_tacks_result_info backTime status riid :" + item.getRiid());
            tasksResultDao.updateNoticeTaskBackInfoStatus(item.getRiid(), sendStatus, detailInfo, sendTime, backTime);
        }
        return true;
    }

}
