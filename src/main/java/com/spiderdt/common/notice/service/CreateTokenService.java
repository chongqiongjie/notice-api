package com.spiderdt.common.notice.service;

import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by qiong on 2017/6/8.
 */
@Service
public class CreateTokenService {

    /**
     * BASE64解密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptBASE64(String key) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(key);
    }


    /**
     * BASE64加密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(byte[] key) throws Exception {
        //base64加密
        //return (new BASE64Encoder()).encodeBuffer(key);

        //url安全的base64加密
        return (Base64.getUrlEncoder().encodeToString(key));
    }


    public static String  hmac(String data) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        String secretKey = "8pbAy1OczORi4HWo6jbgi0zQVusj02";
        //get the bytes of the hmac key and data string
        byte[] secretByte = secretKey.getBytes("UTF-8");
        byte[] dataBytes = data.getBytes("UTF-8");
        SecretKey secret = new SecretKeySpec(secretByte, "HMACSHA256");

        mac.init(secret);
        byte[] doFinal = mac.doFinal(dataBytes);
        byte[] hexB = new Hex().encode(doFinal);
        String checksum = new String(hexB);
        return checksum;
    }

    public static String getToken(String base64_accessKey,String base64_hmac,String data){
        String token = base64_accessKey + ":" + base64_hmac + ":" + data;
        return token;
    }


    public static void main(String[] args) throws Exception {
        String accessKey = "pKPgJj2BmU9Q5R7u";
        Map<String, String> params = new HashMap();
        String paramsString = JSON.toJSONString(params);
        System.out.println(paramsString);

        String data = CreateTokenService.encryptBASE64(paramsString.getBytes());
        System.out.println("加密后的json：" + data);

        String base64_accessKey = CreateTokenService.encryptBASE64(accessKey.getBytes());
        System.out.println("base64_accessKey:" + base64_accessKey);



        byte[] byteArray = CreateTokenService.decryptBASE64(data);
        System.out.println("解密后：" + new String(byteArray));


        String hmac = CreateTokenService.hmac(data);
        System.out.println("hmac:" + hmac);

        String base64_hmac = CreateTokenService.hmac(hmac);
        System.out.println("base64_hmac:" + base64_hmac);

//        String token = base64_accessKey + ":" + base64_hmac + ":" + data;
//        //String token = base64_accessKey + base64_hmac + data;
//        System.out.println("token:" + token);
        String token = CreateTokenService.getToken(base64_accessKey,base64_hmac,data);
        System.out.println("token:" + token);
    }

}

