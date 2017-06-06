package com.spiderdt.common.notice.entity;

import java.io.Serializable;

/**
 * Created by fivebit on 2017/6/1.
 */
public class ShortUrlReqEntity implements Serializable {

    private static final long serialVersionUID = -8039686696076337053L;
    private Integer status_code;
    private String status_txt;
    private ShortUrlMap data;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getStatus_code() {
        return status_code;
    }

    public void setStatus_code(Integer status_code) {
        this.status_code = status_code;
    }

    public String getStatus_txt() {
        return status_txt;
    }

    public void setStatus_txt(String status_tx) {
        this.status_txt = status_tx;
    }

    public ShortUrlMap getData() {
        return data;
    }

    public void setData(ShortUrlMap data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ShortUrlReqEntity{" +
                "status_code=" + status_code +
                ", status_tx='" + status_txt + '\'' +
                ", data=" + data +
                '}';
    }

    public static class ShortUrlMap{
        private String long_url;
        private String url;

        public String getLong_url() {
            return long_url;
        }

        public void setLong_url(String long_url) {
            this.long_url = long_url;
        }

        public String getUrl() {
            return url;
        }

        @Override
        public String toString() {
            return "ShortUrlMap{" +
                    "long_url='" + long_url + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }

        public void setUrl(String url) {
            this.url = url;
        }

    }
}
