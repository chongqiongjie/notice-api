package com.spiderdt.common.notice.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by qiong on 2017/6/15.
 */
public class Interceptors implements HandlerInterceptor{
    private static final Logger logger = LoggerFactory.getLogger(Interceptors.class);

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handle) throws Exception {
        logger.info("preHandle run cross orggin");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type,token");
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,PUT,DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Content-Type", "application/json;charset=utf-8");
//		response.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type");
        return true;
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handle, Exception e)
            throws Exception {
        // TODO Auto-generated method stub
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handle, ModelAndView model)
            throws Exception {
        // TODO Auto-generated method stub

    }
}




