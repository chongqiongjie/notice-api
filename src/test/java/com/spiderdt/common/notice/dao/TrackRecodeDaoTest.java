package com.spiderdt.common.notice.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author ranran
 * @version V1.0
 * @Title:
 * @Package com.spiderdt.common.notice.dao
 * @Description:
 * @date 2017/6/14 18:09
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class TrackRecodeDaoTest {

    @Autowired
    private TrackRecodeDao trackRecodeDao;

    @Test
    public void testTrackRecodeDao() throws Exception {
        long updateTime = new Date().getTime();

        boolean result = trackRecodeDao.updateTrackRecodeStatus("2CAC3CB01D27CD03A63B856DE49CA6BA", 5, (new Timestamp(updateTime).toString()));
        System.out.println(result);
    }

}
