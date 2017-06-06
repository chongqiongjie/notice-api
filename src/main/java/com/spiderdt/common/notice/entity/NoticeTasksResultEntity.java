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
    private String trackUrlSuffix;
    private String address ;
    private String message ;
    private String sendStatus ;
    private String detailInfo ;
    private String isOpen ;
    private String isClick;
    private String openTime;
    private String clickTime;
    private String submitTime;
    private String sendTime;
    private String backTime;

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public String getTrackUrlSuffix() {
        return trackUrlSuffix;
    }

    public void setTrackUrlSuffix(String trackUrlSuffix) {
        this.trackUrlSuffix = trackUrlSuffix;
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

    public String getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(String isOpen) {
        this.isOpen = isOpen;
    }

    public String getIsClick() {
        return isClick;
    }

    public void setIsClick(String isClick) {
        this.isClick = isClick;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getClickTime() {
        return clickTime;
    }

    public void setClickTime(String clickTime) {
        this.clickTime = clickTime;
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
                "rid=" + rid +
                ", taskId=" + taskId +
                ", msgid='" + msgid + '\'' +
                ", trackUrlSuffix='" + trackUrlSuffix + '\'' +
                ", address='" + address + '\'' +
                ", message='" + message + '\'' +
                ", sendStatus='" + sendStatus + '\'' +
                ", detailInfo='" + detailInfo + '\'' +
                ", isOpen='" + isOpen + '\'' +
                ", isClick='" + isClick + '\'' +
                ", openTime='" + openTime + '\'' +
                ", clickTime='" + clickTime + '\'' +
                ", submitTime='" + submitTime + '\'' +
                ", sendTime='" + sendTime + '\'' +
                ", backTime='" + backTime + '\'' +
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
