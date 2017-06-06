package com.spiderdt.common.notice.resource.interfactBean;

import java.io.Serializable;

/**
 * Created by fivebit on 2017/6/5.
 * 创建任务时，接收的bean
 */
public class TaskInput implements Serializable {

    private static final long serialVersionUID = -8039686696076337054L;
    private String taskType;
    private String clientId;
    private String userId;
    private String addresses;
    private String message;

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

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    @Override
    public String toString() {
        return "TaskInput{" +
                "taskType='" + taskType + '\'' +
                ", clientId='" + clientId + '\'' +
                ", userId='" + userId + '\'' +
                ", addresses='" + addresses + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
