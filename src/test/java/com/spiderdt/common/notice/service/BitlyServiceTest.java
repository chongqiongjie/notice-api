package com.spiderdt.common.notice.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author ranran
 * @version V1.0
 * @Title:
 * @Package com.spiderdt.common.notice.service
 * @Description:
 * @date 2017/6/15 16:22
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class BitlyServiceTest {

    @Autowired
    BitlyService bitlyService;

    @Test
    public void getShortUrl() throws Exception {

        String shortUrl = bitlyService.getShortUrl("http://www.baidu.com/track/9e34b9e1e4b3294de6f2cf4d5adbee5275f6d7f22b30e4b2cac5e659b8358e6c");
        System.out.println(shortUrl);

    }

    @Test
    public void getShortUrlBatch() throws Exception {
    }

    @Test
    public void getResutlFromHttp() throws Exception {
    }

}