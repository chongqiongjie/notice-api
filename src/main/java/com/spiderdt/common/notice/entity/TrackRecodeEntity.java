package com.spiderdt.common.notice.entity;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by fivebit on 2017/6/3.
 */
public class TrackRecodeEntity implements Serializable {

    private static final long serialVersionUID = -8039686696076337053L;
    private String trackUrlSuffix;
    private String trackType;
    private String urlOrg;
    private Integer taskId;
    private String riid;
    private String messageReplace;
    private String messageOrg;
    private String params;
    private Map<String,String> mapParams;
    private Integer isClick;
    private String clieckTime;

    @Override
    public String toString() {
        return "TrackRecodeEntity{" +
                "trackUrlSuffix='" + trackUrlSuffix + '\'' +
                ", trackType='" + trackType + '\'' +
                ", urlOrg='" + urlOrg + '\'' +
                ", taskId=" + taskId +
                ", riid='" + riid + '\'' +
                ", messageReplace='" + messageReplace + '\'' +
                ", messageOrg='" + messageOrg + '\'' +
                ", params='" + params + '\'' +
                ", mapParams=" + mapParams +
                ", isClick=" + isClick +
                ", clieckTime='" + clieckTime + '\'' +
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

    public String getTrackType() {
        return trackType;
    }

    public void setTrackType(String trackType) {
        this.trackType = trackType;
    }

    public String getRiid() {
        return riid;
    }

    public void setRiid(String riid) {
        this.riid = riid;
    }

    public Integer getIsClick() {
        return isClick;
    }

    public void setIsClick(Integer isClick) {
        this.isClick = isClick;
    }

    public String getClieckTime() {
        return clieckTime;
    }

    public void setClieckTime(String clieckTime) {
        this.clieckTime = clieckTime;
    }

    public void setMapParams(Map<String, String> mapParams) {
        this.mapParams = mapParams;
    }

}
