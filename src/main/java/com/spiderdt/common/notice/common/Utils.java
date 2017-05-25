package com.spiderdt.common.notice.common;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * Created by fivebit on 2017/5/11.
 * common tools about string/date/others
 */
public class Utils {
    private static Logger log = LoggerFactory.getLogger(Utils.class);
    /**
     * 检测token的格式，要符合xxxxxxxx-xxxx-xxxx-xxxxxx-xxxxxxxxxx
     * @param token
     * @return true/false
     */
    public static Boolean checkTokenFormat(String token){
        Boolean status = true;
        if(token == null || token.isEmpty() == true || token.length() !=36 ){
            status = false;
        }else {
            status = token.matches("[a-zA-Z0-9]{8}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{12}");
        }
        log.info("check token format:"+token+" and ret:"+status);
        return status;
    }

    /**
     * 规范过期时间，在0-1800之间
     * @param expire_ts
     * @return
     */
    public static int formatExpiredTime(int expire_ts){
        int default_et= 1800;
        if(expire_ts > 0 && expire_ts < 1800){
            return expire_ts;
        }
        return default_et;
    }

    public static Boolean checkAuthBearerHeader(String authHeader){
        return true;
    }
    public static Boolean checkAuthBasicaHeader(String authHeader){
        return true;
    }

    public static String encodePassword(String password){
        //return  Jencode.MD5(password);
        //return  new BCryptPasswordEncoder().encode(password);

        return null;
    }
    public static String getNewToken(){
        return UUID.randomUUID().toString();
    }

    /**
     * 检测password是否符合要求，eg. length,
     * @param password
     * @return
     */
    public static Boolean checkPasswordFormat(String password){
        return true;
    }
    public static String getRespons(int status,String code,Object data){
        JSONObject oper = new JSONObject();
        oper.put("status",status);
        oper.put("code",code);
        oper.put("data",data);
        return oper.toJSONString();
    }
    public static String getRespons(String code,Object data){
        return Utils.getRespons(200,code,data);

    }
    public static String getRespons(Object data){
        return Utils.getRespons("0",data);

    }
    public static String getRespons(){
        return Utils.getRespons("0","");

    }
    public static Boolean checkString(String st){
        if( null == st || st.isEmpty() == true ){
            return false;
        }
        return true;
    }
    public static String replaceString(String src,String rep,int start,int length){
        if(src.length()<start+length){
            log.error("replace index error:src:"+src+" start:"+start+" length:"+length);
            return src;
        }
        char[] src_char = src.toCharArray();
        char[] rep_char = rep.toCharArray();
        int ret_char_len = src.length()-length+rep.length();    //结果长度
        char[] ret_char = new char[ret_char_len];               //结果数组
        int i = 0;
        for(i=0;i<start;i++){
            ret_char[i] = src_char[i];
        }
        for(i=0;i<rep.length();i++){
            ret_char[i+start] = rep_char[i];
        }
        for(i=0;i<src.length()-length-start;i++){
            ret_char[i+start+rep.length()] = src_char[i+start+length];
        }
        return String.valueOf(ret_char);
    }

}
