package com.spiderdt.common.notice.entity;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by fivebit on 2017/6/3.
 */
public class TrackRecodeEntity implements Serializable {

    private static final long serialVersionUID = -8039686696076337053L;

    private String urlEncode;
    private String urlOrg;
    private String params;
    private Map<String,String> mapParams;
    private Integer status;

    @Override
    public String toString() {
        return "TrackRecodeEntity{" +
                "urlEncode='" + urlEncode + '\'' +
                ", urlOrg='" + urlOrg + '\'' +
                ", params='" + params + '\'' +
                ", mapParams=" + mapParams +
                ", status=" + status +
                '}';
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

    public String getUrlEncode() {
        return urlEncode;
    }

    public void setUrlEncode(String urlEncode) {
        this.urlEncode = urlEncode;
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
