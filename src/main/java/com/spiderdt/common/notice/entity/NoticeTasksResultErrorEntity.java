package com.spiderdt.common.notice.entity;

import java.io.Serializable;

/**
 * @author ranran
 * @version V1.0
 * @Title:
 * @Package com.spiderdt.common.notice.entity
 * @Description:
 * @date 2017/6/29 10:10
 */
public class NoticeTasksResultErrorEntity implements Serializable{

    private static final long serialVersionUID = -8039686696076337053L;
    private int taskId ;
    private String address ;
    private String sendStatus ;
    private String detailInfo ;
    private String backTime;

    public NoticeTasksResultErrorEntity(int taskId, String address, String sendStatus, String detailInfo, String backTime) {
        this.taskId = taskId;
        this.address = address;
        this.sendStatus = sendStatus;
        this.detailInfo = detailInfo;
        this.backTime = backTime;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }

    public String getDetailInfo() {
        return detailInfo;
    }

    public void setDetailInfo(String detailInfo) {
        this.detailInfo = detailInfo;
    }

    public String getBackTime() {
        return backTime;
    }

    public void setBackTime(String backTime) {
        this.backTime = backTime;
    }

    @Override
    public String toString() {
        return "NoticeTasksResultErrorEntity{" +
                "taskId=" + taskId +
                ", address='" + address + '\'' +
                ", sendStatus='" + sendStatus + '\'' +
                ", detailInfo='" + detailInfo + '\'' +
                ", backTime='" + backTime + '\'' +
                '}';
    }
}
