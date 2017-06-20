package com.spiderdt.common.notice.controller;

/**
 * Created by qiong on 2017/6/8.
 */

import com.alibaba.fastjson.JSONObject;
import com.spiderdt.common.notice.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class EmailController {
  @Autowired
  FileService fileService;
    @RequestMapping(value = "/createplan/combinesubplan", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity download(@RequestBody Map<String,Object> params){
        JSONObject response = new JSONObject();
        String urlString = (String) params.get("urlString");
        fileService.download(urlString, "chong","chong");
        response.put("status","success");
        return ResponseEntity.status(HttpStatus.OK).body(response.toString());
    }

}
