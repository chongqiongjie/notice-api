package com.spiderdt.common.notice.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by qiong on 2017/6/8.
 */
@Controller
public class CreateTokenController {


    @RequestMapping(value = "/token", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> download(@RequestBody Map<String,String> params){

        JSONObject response = new JSONObject();
        System.out.println(JSON.toJSONString(params));

        response.put("status", "success");

        return ResponseEntity.ok(response.toJSONString());
    }

}
