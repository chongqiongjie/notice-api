package com.spiderdt.common.notice.controller;

/**
 * Created by qiong on 2017/6/8.
 */

import com.alibaba.fastjson.JSONObject;
import com.spiderdt.common.notice.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class EmailController {
  @Autowired
    EmailService emailService;
    @RequestMapping(value = "/createplan/combinesubplan", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity download(@RequestBody Map<String,Object> params){
        JSONObject response = new JSONObject();
        String urlString = (String) params.get("urlString");
        emailService.download(urlString,"chong");
        response.put("status","success");
        return ResponseEntity.status(HttpStatus.OK).body(response.toString());
    }

}
