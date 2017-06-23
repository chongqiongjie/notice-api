package com.spiderdt.common.notice.entity;

/**
 * Created by qiong on 2017/6/13.
 */
public class SmsTemplateEntity {
    private Integer tid;
    private String tempName;
    private String messageType;
    private String tempType;
    private String userId;
    private String templateSubject;
    private String templateContent;
    private String createTime;
    private String updateTime;
    private Integer isValid;

    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }

    public String getTempName() {
        return tempName;
    }

    public void setTempName(String tempName) {
        this.tempName = tempName;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getTempType() {
        return tempType;
    }

    public void setTempType(String tempType) {
        this.tempType = tempType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTemplateSubject() {
        return templateSubject;
    }

    public void setTemplateSubject(String templateSubject) {
        this.templateSubject = templateSubject;
    }

    public String getTemplateContent() {
        return templateContent;
    }

    public void setTemplateContent(String templateContent) {
        this.templateContent = templateContent;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }

    @Override
    public String toString() {
        return "SmsTemplateEntity{" +
                "tid=" + tid +
                ", tempName='" + tempName + '\'' +
                ", messageType='" + messageType + '\'' +
                ", tempType='" + tempType + '\'' +
                ", userId='" + userId + '\'' +
                ", templateSubject='" + templateSubject + '\'' +
                ", templateContent='" + templateContent + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", isValid=" + isValid +
                '}';
    }
}
