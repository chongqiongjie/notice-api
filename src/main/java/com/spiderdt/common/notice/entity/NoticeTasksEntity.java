package com.spiderdt.common.notice.entity;

import com.spiderdt.common.notice.common.AppConstants;
import com.spiderdt.common.notice.common.Jdate;
import com.spiderdt.common.notice.resource.interfactBean.TaskInput;

import java.io.Serializable;

/**
 * Created by fivebit on 2017/5/19.
 */
public class NoticeTasksEntity implements Serializable {

    private static final long serialVersionUID = -8039686696076337053L;
    private Integer taskId ;
    private Integer parentTaskId ;
    private String taskType ;
    private String clientId ;
    private String userId ;
    private String jobId;
    private String addresses ;
    private Integer templateId;
    private String attachments;
    private String subject;
    private String message ;
    private String status ;
    private String createTime ;
    private String updateTime ;


    public void initByTaskInput(TaskInput input){
        this.parentTaskId = 0;
        this.taskType = input.getTask_type();
        this.clientId = input.getClient_id();
        this.userId = input.getUser_id();
        this.jobId = input.getJob_id();
        this.templateId = input.getTemplate_id();
        this.attachments = input.getAttachments();
        this.subject = input.getSubject();
        this.message = input.getMessage();
        this.status = AppConstants.TASK_STATUS_INIT;        //job init
        this.createTime = Jdate.getNowStrTime();
        this.updateTime = Jdate.getNowStrTime();
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(Integer parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
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

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getAddresses() {
        return addresses;
    }

    public void setAddresses(String addresses) {
        this.addresses = addresses;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "NoticeTasksEntity{" +
                "taskId=" + taskId +
                ", parentTaskId=" + parentTaskId +
                ", taskType='" + taskType + '\'' +
                ", clientId='" + clientId + '\'' +
                ", userId='" + userId + '\'' +
                ", addresses='" + addresses + '\'' +
                ", message='" + message + '\'' +
                ", attachments='" + attachments + '\'' +
                ", status='" + status + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }

}
