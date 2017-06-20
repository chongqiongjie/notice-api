package com.spiderdt.common.notice.service;

import com.spiderdt.common.notice.entity.TrackRecodeEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

/**
 * @author ranran
 * @version V1.0
 * @Title:
 * @Package com.spiderdt.common.notice.service
 * @Description:
 * @date 2017/6/15 9:59
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class UrlServiceTest {
    @Autowired
    UrlService urlService;

    @Test
    public void makeTrackUrlPair() throws Exception {
    }

    @Test
    public void makeShortUrl() throws Exception {
    }

    @Test
    public void makeTrackUrlPair1() throws Exception {
    }

    @Test
    public void saveTrackPairUrlToDb() throws Exception {
    }

    @Test
    public void saveTrackPairUrlToRedis() throws Exception {
    }

//    @Test
//    public void getTrackUrlOrgInfoByEncrypt() throws Exception {
//        Map<String,String> org_url_and_params = urlService.getTrackUrlOrgInfoByEncrypt("track_url_suffix_test");
//        System.out.println(org_url_and_params);
//    }

//    @Test
//    public void getTrackRecodeByEncrypt() throws Exception {
//
////        TrackRecodeEntity trackRecodeEntity = urlService.getTrackRecodeByEncrypt("BCE955064F90DCDE086E61BC17E093D2");
//        TrackRecodeEntity trackRecodeEntity1 = urlService.getTrackRecodeByEncrypt("BCE955064F90DCDE086E61BC17E093D2");
//
//
//    }

    @Test
    public void updateTaskResultByTrackInfo() throws Exception {
    }



}