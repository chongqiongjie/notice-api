package com.spiderdt.common.notice.service;

import com.alibaba.fastjson.JSONObject;
import com.spiderdt.common.notice.common.JhttpClient;
import com.spiderdt.common.notice.common.Jlog;
import com.spiderdt.common.notice.common.Utils;
import com.spiderdt.common.notice.entity.ShortUrlReqEntity;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fivebit on 2017/6/1.
 */
@Service("bitlyService")
public class BitlyService {
    private String api_url = "https://api-ssl.bitly.com/v3/link/lookup?";
    private String method = "GET";
    private String access_token = "access_token=7d4ad6743932d11b42894175e21f401345878253";
    private String long_url = "";     //可以多个&url=xxx&url=***
    private Integer batch = 5;       //每次只能查询15个

    /**
     * 批量获取短链接
     * @param items
     * @return map 对
     */
    public Map<String,String> getShortUrlBatch(List<String> items){
        Jlog.info("begin get short url:"+items.toString());
        Map<String,String> rets = new HashMap<String,String>(items.size());
        if(null == items || items.size() ==0){
            return rets;
        }
        int i = 0;
        StringBuffer path = new StringBuffer();
        path.append(api_url);
        path.append(access_token);
        for(String item:items){
            i++;
            path.append("&url="+ URLEncoder.encode(item));
            if(i == batch){
                i = 0;
                Jlog.info("send to http service:"+path.toString());
                rets = Utils.mergMap(rets,getResutlFromHttp(path.toString()));
                Jlog.info("get from http service:"+rets.toString());
                path.delete(0,path.length());
                path.append(api_url);
                path.append(access_token);
            }

        }
        if(i != 0 ){
            Jlog.info("send to http service:"+path.toString());
            rets = Utils.mergMap(rets,getResutlFromHttp(path.toString()));
            Jlog.info("get from http service:"+rets.toString());

        }
        Jlog.info("get ret from short url service:"+rets.toString());
        return rets;
    }

    public Map<String,String> getResutlFromHttp(String url){
        Map<String,String> rets = null;
        JSONObject  short_json = JhttpClient.httpGet(url);
        Jlog.info("get req from http service:"+short_json.toJSONString());
        if(short_json == null){
            return rets;
        }
        ShortUrlReqEntity shortUrlReqEntity = JSONObject.toJavaObject(short_json,ShortUrlReqEntity.class);
        Jlog.info("short url format:"+shortUrlReqEntity.toString());
        rets = new HashMap<String,String>();
        String lookup_key = "link_lookup";
        if(shortUrlReqEntity.getStatus_code() == 200){
            List<ShortUrlReqEntity.ShortUrlMap> maps = shortUrlReqEntity.getData().get(lookup_key);
            for(ShortUrlReqEntity.ShortUrlMap map:maps){
                rets.put(map.getUrl(),map.getAggregate_link());
            }
        }else{
            List<ShortUrlReqEntity.ShortUrlMap> maps = shortUrlReqEntity.getData().get(lookup_key);
            for(ShortUrlReqEntity.ShortUrlMap map:maps){
                rets.put(map.getUrl(),"error:"+shortUrlReqEntity.getStatus_txt());
            }

        }
        return rets;

    }


}
