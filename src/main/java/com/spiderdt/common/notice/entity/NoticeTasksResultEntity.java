package com.spiderdt.common.notice.entity;

import java.io.Serializable;

/**
 * Created by fivebit on 2017/5/21.
 */
public class NoticeTasksResultEntity implements Serializable {

    private static final long serialVersionUID = -8039686696076337053L;
    private String riid;
    private Integer taskId ;
    private String address ;
    private String message ;
    private String subject;
    private String sendStatus ;
    private String detailInfo ;
    private String submitTime;
    private String sendTime;
    private String backTime;
    // other value
    private String taskType;


    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getRiid() {
        return riid;
    }

    public void setRiid(String riid) {
        this.riid = riid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDetailInfo() {
        return detailInfo;
    }

    public void setDetailInfo(String detailInfo) {
        this.detailInfo = detailInfo;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    @Override
    public String toString() {
        return "NoticeTasksResultEntity{" +
                "riid='" + riid + '\'' +
                ", taskId=" + taskId +
                ", address='" + address + '\'' +
                ", message='" + message + '\'' +
                ", subject='" + subject + '\'' +
                ", sendStatus='" + sendStatus + '\'' +
                ", detailInfo='" + detailInfo + '\'' +
                ", submitTime='" + submitTime + '\'' +
                ", sendTime='" + sendTime + '\'' +
                ", backTime='" + backTime + '\'' +
                ", taskType='" + taskType + '\'' +
                '}';
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getBackTime() {
        return backTime;
    }

    public void setBackTime(String backTime) {
        this.backTime = backTime;
    }


}
