package com.spiderdt.common.notice.resource;

import com.spiderdt.common.notice.common.Jlog;
import com.spiderdt.common.notice.common.Utils;
import com.spiderdt.common.notice.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Map;

/**
 * Created by fivebit on 2017/6/1.
 * 监控短信／邮件到达率等
 */
@Path("/track")
@Component
public class TrackResource {
    @Autowired
    private UrlService urlService;
    /**
     * track encrypt url。from sms／email open or click.
     * log and update db record
     * @param encrypt_url
     * @return
     */
    @GET
    @Path("/{encrypt_url}")
    public Response trackInfo(@PathParam("encrypt_url") String encrypt_url){
        Jlog.info("do track url begin:"+encrypt_url);
        Map<String,String> trackUrlOrgInfo = urlService.getTrackUrlOrgInfoByEncrypt(encrypt_url);
        Jlog.info("track org info:"+trackUrlOrgInfo);
        urlService.updateTaskResultByTrackInfo(trackUrlOrgInfo);
        Jlog.info("do track url end:"+encrypt_url);
        String orgUrl =  trackUrlOrgInfo.get("org_url");
        if(orgUrl.startsWith("img_track_url")) {
            // open 图片 src
            Jlog.info("email open ------------------------------------");
            return Response.status(Response.Status.CREATED)// 201
                    .entity(Utils.getRespons()).build();
        }else {
            // click url
            Jlog.info("email link click " + orgUrl + " ------------------------------------");
            return Response.seeOther(URI.create(trackUrlOrgInfo.get("org_url"))).build();
        }
    }
}
