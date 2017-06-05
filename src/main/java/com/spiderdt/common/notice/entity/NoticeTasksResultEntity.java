package com.spiderdt.common.notice.entity;

import java.io.Serializable;

/**
 * Created by fivebit on 2017/5/21.
 */
public class NoticeTasksResultEntity implements Serializable {

    private static final long serialVersionUID = -8039686696076337053L;
    private Integer rid;
    private Integer taskId ;
    private String msgid ;
    private String address ;
    private String status ;
    private String isReceived;
    private String detailInfo;
    private String submitTime;
    private String sendTime;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsReceived() {
        return isReceived;
    }

    public void setIsReceived(String isReceived) {
        this.isReceived = isReceived;
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

    @Override
    public String toString() {
        return "NoticeTasksResultEntity{" +
                "taskId=" + taskId +
                ", msgid='" + msgid + '\'' +
                ", address='" + address + '\'' +
                ", status='" + status + '\'' +
                ", isReceived='" + isReceived + '\'' +
                ", detailInfo='" + detailInfo + '\'' +
                ", submitTime='" + submitTime + '\'' +
                ", sendTime='" + sendTime + '\'' +
                ", backTime='" + backTime + '\'' +
                '}';
    }

    private String backTime ;
}
