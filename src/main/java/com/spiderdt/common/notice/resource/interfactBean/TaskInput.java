package com.spiderdt.common.notice.resource.interfactBean;

import java.io.Serializable;

/**
 * Created by fivebit on 2017/6/5.
 * 创建任务时，接收的bean
 */
public class TaskInput implements Serializable {

    private static final long serialVersionUID = -8039686696076337054L;
    private String task_type;
    private String client_id;
    private String user_id;
    private String job_id;
    private Integer template_id;
    private String message;
    private String subject = "";
    private String attachments = "";

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getTask_type() {
        return task_type;
    }

    public void setTask_type(String task_type) {
        this.task_type = task_type;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(Integer template_id) {
        this.template_id = template_id;
    }

    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return "TaskInput{" +
                "task_type='" + task_type + '\'' +
                ", client_id='" + client_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", job_id='" + job_id + '\'' +
                ", message='" + message + '\'' +
                ", template_id='" + template_id + '\'' +
                ", attachments='" + attachments + '\'' +
                ", subject='" + subject + '\'' +
                '}';
    }
}
