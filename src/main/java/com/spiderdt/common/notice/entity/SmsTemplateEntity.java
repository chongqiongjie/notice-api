package com.spiderdt.common.notice.entity;

/**
 * Created by qiong on 2017/6/13.
 */
public class SmsTemplateEntity {
    private Integer tid;
    private String temp_name;
    private String message_type;
    private String temp_type;
    private String user_id;
    private String template_subject;
    private String template_content;
    private String create_time;
    private String update_time;
    private Integer is_valid;

    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }

    public String getTemp_name() {
        return temp_name;
    }

    public void setTemp_name(String temp_name) {
        this.temp_name = temp_name;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public String getTemp_type() {
        return temp_type;
    }

    public void setTemp_type(String temp_type) {
        this.temp_type = temp_type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTemplate_subject() {
        return template_subject;
    }

    public void setTemplate_subject(String template_subject) {
        this.template_subject = template_subject;
    }

    public String getTemplate_content() {
        return template_content;
    }

    public void setTemplate_content(String template_content) {
        this.template_content = template_content;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }


    public Integer getIs_valid() {
        return is_valid;
    }

    public void setIs_valid(Integer is_valid) {
        this.is_valid = is_valid;
    }

    @Override
    public String toString() {
        return "SmsTemplateEntity{" +
                "tid=" + tid +
                ", temp_name='" + temp_name + '\'' +
                ", message_type='" + message_type + '\'' +
                ", temp_type='" + temp_type + '\'' +
                ", user_id='" + user_id + '\'' +
                ", template_subject='" + template_subject + '\'' +
                ", template_content='" + template_content + '\'' +
                ", create_time='" + create_time + '\'' +
                ", update_time='" + update_time + '\'' +
                ", is_valid=" + is_valid +
                '}';
    }
}
