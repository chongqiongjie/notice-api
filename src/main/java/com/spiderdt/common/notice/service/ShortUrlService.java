package com.spiderdt.common.notice.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.spiderdt.common.notice.common.JhttpClient.httpGet;
import static com.spiderdt.common.notice.common.JhttpClient.httpPost;

/**
 * Created by qiong on 2017/6/28.
 */
@Service
public class ShortUrlService {


//    /**
//     * 获取临时token
//     * @param client_id
//     * @return
//     */
//    public String Oauth2Token(String client_id,String redirect_uri) throws IOException {
//        System.out.println(client_id);
//       // String url = "https://api.weibo.com/oauth2/authorize?" + "client_id=" + client_id + "&redirect_uri=" + redirect_uri;
//        URL url = new URL("https://api.weibo.com/oauth2/authorize?" + "client_id=" + client_id + "&redirect_uri=" + redirect_uri);
//        System.out.println(url);
//        //JSONObject http = httpGet(url);
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setInstanceFollowRedirects(false);
//        conn.connect();
//        conn.getResponseCode();
//        System.out.println("headers:  " + conn.getResponseCode());
//        Map headers = conn.getHeaderFields();
//        Set<String> keys = headers.keySet();
//        for( String key : keys ){
//            String val = conn.getHeaderField(key);
//            System.out.println(key+"    "+val);
//        }
//
//
//        return null;
//    }
//
//
//    /**
//     * 获取access_token
//     * @param client_id
//     * @param client_secret
//     * @param grant_type 调用authorize获得的code值
//     * @return
//     */
//    public String Oauth2AccessToken(String client_id, String client_secret, String grant_type){
//        String url = "https://api.weibo.com/oauth2/access_token?";
//        JSONObject params = new JSONObject();
//        params.put("client_id",client_id);
//        params.put("client_secret",client_secret);
//        params.put("grant_type",grant_type);
//        JSONObject http = httpPost(url,params);
//        return http.toString();
//    }


    /**
     * long_url -> short_url
     * @param long_url(URLEncoder)
     * @return
     * @throws Exception
     */
    public Map<String, String> LongUrlToShortUrl(List<String> long_url) throws Exception {

        Map<String, String> longShortMap = new HashMap<>();

        String ak = null;
        ArrayList<String> aks = new ArrayList<>();
        aks.add("2.005IvWNGoAN9dD586707c2a90CCEPa");


        Random random = new Random();
        int index = random.nextInt(aks.size());
        ak = aks.get(index);

        String url = "https://api.weibo.com/2/short_url/shorten.json?" + "access_token=" + ak ;


        int i = 0;
        StringBuilder requestUrl = new StringBuilder(url);
        System.out.println("requestUrl:" + requestUrl);


        while (long_url.size() > 0) {

            i++;
            String lastUrl = long_url.get(long_url.size() - 1);
            long_url.remove(lastUrl);
            requestUrl.append("&url_long=").append(lastUrl);

            if (i % 20 == 0) {
                longShortMap.putAll(getShortUrl(requestUrl.toString()));
                requestUrl = new StringBuilder(url);

            }

        }
        // send
        longShortMap.putAll(getShortUrl(requestUrl.toString()));


        return longShortMap;
    }


    private Map<String, String> getShortUrl(String url) {

        Map<String, String> longShortMap = new HashMap<>();

        JSONObject results = httpGet(url);

        JSONArray urls = results.getJSONArray("urls");

        for (int i = 0; i < urls.size(); i++) {
            JSONObject urlMap = urls.getJSONObject(i);
            longShortMap.put(urlMap.getString("url_long"), urlMap.getString("url_short"));
        }
       // System.out.println("longShortMap:" + longShortMap);
        return longShortMap;
    }


}
