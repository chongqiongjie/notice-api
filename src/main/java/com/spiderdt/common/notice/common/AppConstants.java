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


    public static final List<String> TASK_STAUTS = new ArrayList<String>(){
        {
            TASK_STAUTS.add("new");     //新创建
            TASK_STAUTS.add("initing"); //开始初始化
            TASK_STAUTS.add("inited");  //初始化完成
            TASK_STAUTS.add("sending"); //开始发送
            TASK_STAUTS.add("sended");  //发送完成
            TASK_STAUTS.add("getret");  //获取结果中
            TASK_STAUTS.add("succ");    //全部成功
            TASK_STAUTS.add("fail");    //部分失败
            TASK_STAUTS.add("failall"); //全部失败
        }
    };

    //短信提交响应错误码
    public static final HashMap<Integer,String> SMS_SUBMIT_CODE_STATUS = new HashMap<Integer,String>(16){
        {
            SMS_SUBMIT_CODE_STATUS.put(0, "提交成功");
            SMS_SUBMIT_CODE_STATUS.put(1, "账号无效");
            SMS_SUBMIT_CODE_STATUS.put(2, "密码错误");
            SMS_SUBMIT_CODE_STATUS.put(3, "msgid太长，不得超过32位");
            SMS_SUBMIT_CODE_STATUS.put(5, "手机号码个数超过最大限制(500个)");
            SMS_SUBMIT_CODE_STATUS.put(6, "短信内容超过最大限制(350字)");
            SMS_SUBMIT_CODE_STATUS.put(7, "扩展子号码无效");
            SMS_SUBMIT_CODE_STATUS.put(8, "定时时间格式错误");
            SMS_SUBMIT_CODE_STATUS.put(14, "手机号码为空");
            SMS_SUBMIT_CODE_STATUS.put(19, "用户被禁发或禁用");
            SMS_SUBMIT_CODE_STATUS.put(20, "IP鉴权失败");
            SMS_SUBMIT_CODE_STATUS.put(21, "短信内容为空");
            SMS_SUBMIT_CODE_STATUS.put(22, "数据包大小不匹配");
            SMS_SUBMIT_CODE_STATUS.put(98, "系统正忙");
            SMS_SUBMIT_CODE_STATUS.put(99, "消息格式错误");
        }
    };
    //短信状态报告错误码
    public static final HashMap<Integer,String> SMS_REPORT_CODE_STATUS = new HashMap<Integer,String>(16){
        {
            SMS_REPORT_CODE_STATUS.put(4,"手机号码无效");
            SMS_REPORT_CODE_STATUS.put(5,"签名不合法");
            SMS_REPORT_CODE_STATUS.put(6,"短信内容超过最大限制");
            SMS_REPORT_CODE_STATUS.put(9,"请求来源地址无效");
            SMS_REPORT_CODE_STATUS.put(10,"内容敏感词");
            SMS_REPORT_CODE_STATUS.put(11,"余额不足");
            SMS_REPORT_CODE_STATUS.put(12,"购买产品或订购还未生效或产品已暂停使用");
            SMS_REPORT_CODE_STATUS.put(13,"账号被禁用或禁发");
            SMS_REPORT_CODE_STATUS.put(14,"不支持该运营商");
            SMS_REPORT_CODE_STATUS.put(16,"发送号码数没有达到该产品的最小发送数");
            SMS_REPORT_CODE_STATUS.put(19,"黑名单号码");
            SMS_REPORT_CODE_STATUS.put(20,"该模版ID已被禁用");
            SMS_REPORT_CODE_STATUS.put(21,"非法模版ID");
            SMS_REPORT_CODE_STATUS.put(22,"不支持的MSGFMT");
            SMS_REPORT_CODE_STATUS.put(23,"子号码无效");
            SMS_REPORT_CODE_STATUS.put(24,"内容为空");
            SMS_REPORT_CODE_STATUS.put(25,"号码为空");
            SMS_REPORT_CODE_STATUS.put(26,"单个号码相同内容限制");
            SMS_REPORT_CODE_STATUS.put(27,"单个号码次数限制");
            SMS_REPORT_CODE_STATUS.put(28,"号码被拦截，不允许发送");
            SMS_REPORT_CODE_STATUS.put(96,"处理失败");
            SMS_REPORT_CODE_STATUS.put(97,"接入方式错误");
            SMS_REPORT_CODE_STATUS.put(98,"系统繁忙");
            SMS_REPORT_CODE_STATUS.put(99,"消息格式错误");
        }
    };


}
