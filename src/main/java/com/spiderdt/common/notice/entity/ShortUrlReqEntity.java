package com.spiderdt.common.notice.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by fivebit on 2017/6/1.
 */
public class ShortUrlReqEntity implements Serializable {

    private static final long serialVersionUID = -8039686696076337053L;
    private Integer status_code;
    private String status_txt;
    private Map<String,List<ShortUrlMap>> data;

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

    public Map<String, List<ShortUrlMap>> getData() {
        return data;
    }

    public void setData(Map<String, List<ShortUrlMap>> data) {
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
        private String aggregate_link;
        private String url;

        public String getAggregate_link() {
            return aggregate_link;
        }

        public void setAggregate_link(String aggregate_link) {
            this.aggregate_link = aggregate_link;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return "ShortUrlMap{" +
                    "aggregate_link='" + aggregate_link + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }
}
