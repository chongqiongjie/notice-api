package com.spiderdt.common.notice.resource;

import com.spiderdt.common.notice.common.Utils;
import com.spiderdt.common.notice.entity.SmsTemplateEntity;
import com.spiderdt.common.notice.service.ManageTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qiong on 2017/6/15.
 */
@Path("/manage")
@Controller
@Produces({ MediaType.APPLICATION_JSON })
public class SmsResource {
   @Autowired
   ManageTemplateService manageTemplateService;

   @GET
   @Path("/query/{message_type}/{user_id}")
   public Response query(@PathParam("message_type") String message_type,@PathParam("user_id") String user_id){
       List<SmsTemplateEntity> template =  manageTemplateService.getSmsTemplate(message_type,user_id);
       System.out.println("template:" + template.size());
       List result = new ArrayList();
       for(int i=0;i<template.size();i++){
           Map res = new HashMap();
           res.put("id",template.get(i).getTid());
           res.put("con",template.get(i).getTemplateContent());
           res.put("name",template.get(i).getTempName());
           result.add(res);


       }

       return  Response.ok().entity(Utils.getRespons("0",result)).build();
   }


    @POST
    @Path("/insert")
    //@Consumes({ MediaType.APPLICATION_JSON })
    public Response insert(SmsTemplateEntity templateEntity){
        boolean template =  manageTemplateService.insertSmsTemplate(templateEntity);
        return  Response.ok().entity(Utils.getRespons("0",template)).build();
    }



    @DELETE
    @Path("/delete/{tid}")
    public Response delete(@PathParam("tid") Integer tid){
        boolean template =  manageTemplateService.deleteSmsTemplate(tid);
        return  Response.ok().entity(Utils.getRespons("0",template)).build();
    }


    @PUT
    @Path("/update/{tid}")
    public Response updateSms(@PathParam("tid") Integer tid,SmsTemplateEntity templateEntity){
        boolean template =  manageTemplateService.updateSmsTemplate(tid,templateEntity.getTemplateContent());
        return  Response.ok().entity(Utils.getRespons("0",template)).build();
    }





}
