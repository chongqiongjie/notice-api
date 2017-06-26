package com.spiderdt.common.notice.resource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.spiderdt.common.notice.common.Utils;
import com.spiderdt.common.notice.service.CreateTokenService;
import com.spiderdt.common.notice.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * Created by qiong on 2017/6/8.
 */
@Path("/uploadfile/token")
@Component
@Produces({ MediaType.APPLICATION_JSON })
public class TokenResource {

//    @Autowired
//    CreateTokenService createTokenService;
//
    @POST
    public Response token(Map<String,String> params){

       // String encodeStr = "";
        String token = "";
        try {
            String accessKey = "pKPgJj2BmU9Q5R7u";
            String paramsString = JSON.toJSONString(params);

            System.out.println("params_before:" + paramsString);
            String data = CreateTokenService.encryptBASE64(paramsString.getBytes());
            System.out.println("params_after:" + data);
            String base64_accessKey = CreateTokenService.encryptBASE64(accessKey.getBytes());
            String hmac = CreateTokenService.hmac(data);
            System.out.println("hmac:" + hmac);
            String base64_hmac = CreateTokenService.encryptBASE64(hmac.getBytes());
            System.out.println("base64_hmac:" + base64_hmac);
             token = CreateTokenService.getToken(base64_accessKey,base64_hmac,data);
            System.out.println("token:" + token);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Response.ok().entity(Utils.getRespons("0",token)).build();
    }

}
