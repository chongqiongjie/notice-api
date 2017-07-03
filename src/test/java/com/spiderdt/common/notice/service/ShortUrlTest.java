package com.spiderdt.common.notice.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiong on 2017/6/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class ShortUrlTest {
    @Autowired
    ShortUrlService shortUrlService;



    @Test
    public void ShortUrl() throws Exception {
        //String access_token = "";
        //String long_url = "https://s.taobao.com/search?q=%E7%8B%97%E7%B2%AE&refpid=430266_1006&source=tbsy&style=grid&tab=all&pvid=b896959255978b89a4caedc18c50c454&clk1=f1880fc3761f79bc0305fccd829f7b06&spm=a21bo.50862.201856-sline.1.Ep854n";
        List<String> long_url = new ArrayList<>();
        long_url.add("https://s.taobao.com/search?q=%E7%BD%91%E7%BA%BF&refpid=430267_1006&source=tbsy&style=grid&tab=all&pvid=e9591feb79cbdcc1dd1e878dd9874ee6&clk1=76b8f85a9e0076da672faf02ad7c5399&spm=a21bo.50862.201856-sline.2.tADhc8");
        long_url.add("https://s.taobao.com/search?q=%E6%B4%97%E8%A1%A3%E6%B6%B2&refpid=430269_1006&source=tbsy&style=grid&tab=all&pvid=438f928e76c7befb749f475158c02489&clk1=416f4b28520be92fc83bb82cc6cf2c5e&spm=a21bo.50862.201856-sline.4.5dcec6f7sUFS0B");
        for (int i = 0; i < 42; i++) {
            long_url.add(i + "");
        }
        System.out.println(shortUrlService.LongUrlToShortUrl(long_url));
    }
}
