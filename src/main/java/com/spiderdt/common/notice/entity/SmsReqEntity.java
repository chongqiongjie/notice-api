package com.spiderdt.common.notice.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fivebit on 2017/5/22.
 */
public class SmsReqEntity implements Serializable {

    private static final long serialVersionUID = -8039686696076337053L;
    private String accout;
    private String password;
    private List<SmsMsgEntity> data ;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getAccout() {
        return accout;
    }

    public void setAccout(String accout) {
        this.accout = accout;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<SmsMsgEntity> getData() {
        return data;
    }

    public void setData(List<SmsMsgEntity> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SmsReqEntity{" +
                "accout='" + accout + '\'' +
                ", password='" + password + '\'' +
                ", data=" + data +
                '}';
    }

    public static class SmsMsgEntity{
        private String msgid;
        private String phones;
        private String content;
        private String sign;
        private String subcode;
        private String sendtime="";

        public String getMsgid() {
            return msgid;
        }

        public void setMsgid(String msgid) {
            this.msgid = msgid;
        }

        public String getPhones() {
            return phones;
        }

        public void setPhones(String phones) {
            this.phones = phones;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getSubcode() {
            return subcode;
        }

        public void setSubcode(String subcode) {
            this.subcode = subcode;
        }

        public String getSendtime() {
            return sendtime;
        }

        public void setSendtime(String sendtime) {
            this.sendtime = sendtime;
        }

        @Override
        public String toString() {
            return "smsMsgEntity{" +
                    "msgid='" + msgid + '\'' +
                    ", phones='" + phones + '\'' +
                    ", content='" + content + '\'' +
                    ", sign='" + sign + '\'' +
                    ", subcode='" + subcode + '\'' +
                    ", sendtime='" + sendtime + '\'' +
                    '}';
        }
    }
}
