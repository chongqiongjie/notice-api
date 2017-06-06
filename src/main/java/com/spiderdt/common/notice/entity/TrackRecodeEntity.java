package com.spiderdt.common.notice.entity;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by fivebit on 2017/6/3.
 */
public class TrackRecodeEntity implements Serializable {

    private static final long serialVersionUID = -8039686696076337053L;
    private Integer taskId;
    private String trackUrlSuffix;
    private String urlOrg;
    private String messageReplace;
    private String messageOrg;
    private String params;
    private Map<String,String> mapParams;
    private Integer status;

    @Override
    public String toString() {
        return "TrackRecodeEntity{" +
                "taskId=" + taskId +
                ", trackUrlSuffix='" + trackUrlSuffix + '\'' +
                ", urlOrg='" + urlOrg + '\'' +
                ", messageReplace='" + messageReplace + '\'' +
                ", messageOrg='" + messageOrg + '\'' +
                ", params='" + params + '\'' +
                ", mapParams=" + mapParams +
                ", status=" + status +
                '}';
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getMessageReplace() {
        return messageReplace;
    }

    public void setMessageReplace(String messageReplace) {
        this.messageReplace = messageReplace;
    }

    public String getTrackUrlSuffix() {
        return trackUrlSuffix;
    }

    public void setTrackUrlSuffix(String trackUrlSuffix) {
        this.trackUrlSuffix = trackUrlSuffix;
    }

    public String getMessageOrg() {
        return messageOrg;
    }

    public void setMessageOrg(String messageOrg) {
        this.messageOrg = messageOrg;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getUrlOrg() {
        return urlOrg;
    }

    public void setUrlOrg(String urlOrg) {
        this.urlOrg = urlOrg;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public Map<String, String> getMapParams() {
        return mapParams;
    }

    public void setMapParams(Map<String, String> mapParams) {
        this.mapParams = mapParams;
    }

}
