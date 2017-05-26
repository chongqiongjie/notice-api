package com.spiderdt.common.notice.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fivebit on 2017/5/26.
 */
public class SmsReportEntity implements Serializable {

    private static final long serialVersionUID = -8039686696076337053L;
    private String result;
    private String desc;
    private List<SmsReportInfoEntity> reports;

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

    public List<SmsReportInfoEntity> getReports() {
        return reports;
    }

    public void setReports(List<SmsReportInfoEntity> reports) {
        this.reports = reports;
    }

    @Override
    public String toString() {
        return "SmsReportEntity{" +
                "result='" + result + '\'' +
                ", desc='" + desc + '\'' +
                ", reports=" + reports +
                '}';
    }

    public static class SmsReportInfoEntity{
        private String msgid;
        private String phone;
        private String status;
        private String desc;
        private String wgcode;
        private String time;
        private String smsCount;
        private String smsIndex;

        public String getMsgid() {
            return msgid;
        }

        public void setMsgid(String msgid) {
            this.msgid = msgid;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
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

        public String getWgcode() {
            return wgcode;
        }

        public void setWgcode(String wgcode) {
            this.wgcode = wgcode;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getSmsCount() {
            return smsCount;
        }

        public void setSmsCount(String smsCount) {
            this.smsCount = smsCount;
        }

        public String getSmsIndex() {
            return smsIndex;
        }

        public void setSmsIndex(String smsIndex) {
            this.smsIndex = smsIndex;
        }

        @Override
        public String toString() {
            return "SmsReportInfoEntity{" +
                    "msgid='" + msgid + '\'' +
                    ", phone='" + phone + '\'' +
                    ", status='" + status + '\'' +
                    ", desc='" + desc + '\'' +
                    ", wgcode='" + wgcode + '\'' +
                    ", time='" + time + '\'' +
                    ", smsCount='" + smsCount + '\'' +
                    ", smsIndex='" + smsIndex + '\'' +
                    '}';
        }
    }
}
