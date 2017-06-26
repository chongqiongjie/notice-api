package com.spiderdt.common.notice.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fivebit on 2017/5/19.
 * 一些静态配置
 */
public class AppConstants {
    public static final int APP_ERROR_CODE = 5055;

    public static final String TASK_STATUS_NEW = "new";
    public static final String TASK_STATUS_INIT = "init";
    public static final String TASK_STATUS_SENDING = "sending";
    public static final String TASK_STATUS_SENDED = "sended";
    public static final String TASK_RESULT_STATUS_NEW = "new";
    public static final String TASK_RESULT_STATUS_SUCCESS = "success";
    public static final String TASK_RESULT_STATUS_AUTH_FAILED = "authFailed";
    public static final String TASK_RESULT_STATUS_FAILED = "failed";
    public static final String SMS_TASK_TYPE = "sms";
    public static final String EMAIL_TASK_TYPE = "email";

    public static final String SMS_ACCOUNT = "dh67651";
    public static final String SMS_PASSWORD = "3376f2615826d0f6125dee096690cbce";

    public static List<String> TASK_STATUS = new ArrayList<String>(){
        {
            add("new");     //新创建
            add("initing"); //开始初始化
            add("inited");  //初始化完成
            add("sending"); //开始发送
            add("sended");  //发送完成
            add("getret");  //获取结果中
            add("succ");    //全部成功
            add("fail");    //部分失败
            add("failall"); //全部失败
        }
    };

    //短信提交响应错误码
    public static final HashMap<Integer,String> SMS_SUBMIT_CODE_STATUS = new HashMap<Integer,String>(16){
        {
            put(0, "提交成功");
            put(1, "账号无效");
            put(2, "密码错误");
            put(3, "msgid太长，不得超过32位");
            put(5, "手机号码个数超过最大限制(500个)");
            put(6, "短信内容超过最大限制(350字)");
            put(7, "扩展子号码无效");
            put(8, "定时时间格式错误");
            put(14, "手机号码为空");
            put(19, "用户被禁发或禁用");
            put(20, "IP鉴权失败");
            put(21, "短信内容为空");
            put(22, "数据包大小不匹配");
            put(98, "系统正忙");
            put(99, "消息格式错误");
        }
    };
    //短信状态报告错误码
    public static final HashMap<Integer,String> SMS_REPORT_CODE_STATUS = new HashMap<Integer,String>(16){
        {
            put(0,"发送成功");
            put(4,"手机号码无效");
            put(5,"签名不合法");
            put(6,"短信内容超过最大限制");
            put(9,"请求来源地址无效");
            put(10,"内容敏感词");
            put(11,"余额不足");
            put(12,"购买产品或订购还未生效或产品已暂停使用");
            put(13,"账号被禁用或禁发");
            put(14,"不支持该运营商");
            put(16,"发送号码数没有达到该产品的最小发送数");
            put(19,"黑名单号码");
            put(20,"该模版ID已被禁用");
            put(21,"非法模版ID");
            put(22,"不支持的MSGFMT");
            put(23,"子号码无效");
            put(24,"内容为空");
            put(25,"号码为空");
            put(26,"单个号码相同内容限制");
            put(27,"单个号码次数限制");
            put(28,"号码被拦截，不允许发送");
            put(96,"处理失败");
            put(97,"接入方式错误");
            put(98,"系统繁忙");
            put(99,"消息格式错误");
        }
    };


}
