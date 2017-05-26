package com.spiderdt.common.notice.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fivebit on 2017/5/26.
 */
public class SmsRespEntity implements Serializable {

    private static final long serialVersionUID = -8039686696076337053L;
    private String result;
    private String desc;
    private List<SmsRespDataEntity> data;

    public static class SmsRespDataEntity{
        private String msgid;
        private String status;
        private String desc;
        private String failPhones;

        public String getMsgid() {
            return msgid;
        }

        public void setMsgid(String msgid) {
            this.msgid = msgid;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getFailPhones() {
            return failPhones;
        }

        public void setFailPhones(String failPhones) {
            this.failPhones = failPhones;
        }

        @Override
        public String toString() {
            return "SmsRespDataEntity{" +
                    "msgid='" + msgid + '\'' +
                    ", status='" + status + '\'' +
                    ", desc='" + desc + '\'' +
                    ", failPhones='" + failPhones + '\'' +
                    '}';
        }
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<SmsRespDataEntity> getData() {
        return data;
    }

    public void setData(List<SmsRespDataEntity> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SmsRespEntity{" +
                "result='" + result + '\'' +
                ", desc='" + desc + '\'' +
                ", data=" + data +
                '}';
    }
}
