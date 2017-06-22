package com.spiderdt.common.notice.entity;

import java.io.Serializable;


public class TestEntity implements Serializable {

//    private static final long serialVersionUID = -8039686696076337053L;
    private int taskId;
    private int noticeCount;
    private int sendCount;
    private int viewCount;
    private int clickCount;



    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getNoticeCount() {
        return noticeCount;
    }

    public void setNoticeCount(int noticeCount) {
        this.noticeCount = noticeCount;
    }

    public int getSendCount() {
        return sendCount;
    }

    public void setSendCount(int sendCount) {
        this.sendCount = sendCount;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    @Override
    public String toString() {
        return "TestEntity{" +
                "taskId=" + taskId +
                ", noticeCount=" + noticeCount +
                ", sendCount=" + sendCount +
                ", viewCount=" + viewCount +
                ", clickCount=" + clickCount +
                '}';
    }
}
