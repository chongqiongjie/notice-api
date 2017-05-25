package com.spiderdt.common.notice.common;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLDecoder;

/**
 * Created by fivebit on 2017/5/19.
 */
public class JhttpClient {
    private static Logger log = LoggerFactory.getLogger(JhttpClient.class);

    public static JSONObject httpPost(String url,JSONObject jsonParam){
        return JhttpClient.httpPost(url, jsonParam, true);
    }
    public static JSONObject httpPost(String url,JSONObject params,Boolean noNeedResponse){
        DefaultHttpClient httpClient = new DefaultHttpClient();
        JSONObject jsonResult = null;
        HttpPost method = new HttpPost(url);
        try {
            if ( null != params ) {
                StringEntity entity = new StringEntity(params.toString(), "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                method.setEntity(entity);
            }
            HttpResponse result = httpClient.execute(method);
            url = URLDecoder.decode(url, "UTF-8");
            if (result.getStatusLine().getStatusCode() == 200) {
                String str = "";
                try {
                    str = EntityUtils.toString(result.getEntity());
                    if (noNeedResponse) {
                        return null;
                    }
                    jsonResult = JSONObject.parseObject(str);
                } catch (Exception e) {
                    log.error("post request error:" + url, e);
                }
            }
        } catch (IOException e) {
            log.error("post request error:" + url, e);
        }
        return jsonResult;
    }
    public static JSONObject httpGet(String url){
        JSONObject jsonResult = null;
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String strResult = EntityUtils.toString(response.getEntity());
                jsonResult = JSONObject.parseObject(strResult);
                url = URLDecoder.decode(url, "UTF-8");
            } else {
                log.error("get request error:" + url);
            }
        } catch (IOException e) {
            log.error("get request error:" + url, e);
        }
        return jsonResult;
    }
}
