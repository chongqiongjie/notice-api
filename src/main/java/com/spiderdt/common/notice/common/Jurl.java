package com.spiderdt.common.notice.common;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * Created by fivebit on 2017/6/3.
 * Url 操作的工具类
 * 包括功能：
 * 1，制作短链。
 * 2，制作跳转URL。
 */
public class Jurl {
    //跟踪URL服务地址
//    public static String track_host = "https://api.spiderdt.com/notice/track/";
    public static String track_host = "http://127.0.0.1:8080/track/";

    public static String makeShortUrl(String url){

        return "http://qiong.com";
    }
    public static Map<String,String> makeShortUrls(List<String> urls){
        return null;
    }

    /**
     * make 跟踪URL的后缀部分。
     * @param dest_url
     * @param params: rid/sc/ac
     * @return
     * eg. dest_url: http://www.baidu.com
     *  params:{"rid":12,"sc":"sms","ac":"open"}
     */
    public static String makeTrackUrlSuffix(String dest_url,Map<String,String> params){
        JSONObject ret = new JSONObject();
        ret.put("dest_url", URLEncoder.encode(dest_url));
        ret.put("params",params);
        Jlog.info("make track url info:"+ret.toJSONString());
        //String encode_part = Jencode.sEncode(ret.toJSONString());
        String suffix_encode_part = Jencode.MD5(ret.toJSONString());
        return suffix_encode_part;
    }
    public static String getCompleteTrackUrl(String suffix){
        return Jurl.track_host+suffix;
    }

    /**
     * 解密跟踪URL
     * @param encrtyp
     * @return
     */
    public static Map<String,Object> decodeTrackUrl(String encrtyp){
        String decode_part = Jencode.sDecode(encrtyp);
        Jlog.info("decode_part:"+ decode_part);
        Map<String,Object> url_info  = null;
        try {
            JSONObject ret = JSONObject.parseObject(decode_part);
            if(ret.containsKey("dest_url") == false || ret.containsKey("params") == false){
                Jlog.error("track url is invalid"+encrtyp+" json:"+decode_part);
            }
            url_info = Maps.newHashMap();
            url_info.put("dest_url",ret.get("dest_url").toString());
            url_info.put("params",(Map<String,String>) ret.get("params"));

        }catch (Exception ee){
            Jlog.error("parse object from string error:"+ee.getMessage());
        }
        return url_info;

    }

}
