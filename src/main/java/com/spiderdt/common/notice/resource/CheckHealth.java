package com.spiderdt.common.notice.resource;

import com.spiderdt.common.notice.common.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by fivebit on 2017/5/10.
 */

@Path("/ping")
@Component
@Produces({ MediaType.APPLICATION_JSON })
public class CheckHealth {
    private static Logger log = LoggerFactory.getLogger(CheckHealth.class);
    @Value("#{appProperties['redis.prefix']}")
    public String name ;
    @GET
    public Response ping(){
        //log.info("OK:"+name+":"+Utils.encodePassword(name));
        String p1 = "testqiong";
        String p2 = "$2a$10$uQQafqXn7N9WNzSp5km3geyeTKjsqLKIco9U8SnpjuHpcUyY9qaLy";
        //Boolean st = new BCryptPasswordEncoder().matches(p1,p2);
        log.info("begin");
        //String t1 = new BCryptPasswordEncoder().encode("4d419464a70d3e4302a4f990e4912299c0eb3ea0ad541707bf3c0f7f3555624005bb9e024cf1a3ee59e23cfaeeab15f603009f0ea00c3254aa6cd0d5ec311c56");
        //log.info("t1:"+t1);
        //st = Jencode.cljCheck(p1,t1);
        //log.info("end2:"+st);
        return Response.ok().entity(Utils.getRespons("0","OK")).build();
    }
}

