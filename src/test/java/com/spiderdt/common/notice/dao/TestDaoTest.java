package com.spiderdt.common.notice.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author ranran
 * @version V1.0
 * @Title:
 * @Package com.spiderdt.common.notice.dao
 * @Description:
 * @date 2017/6/22 11:18
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class TestDaoTest {

    @Autowired
    private TestDao testDao;

    @Test
    public void getSummaryByTaskId() throws Exception {
        System.out.println(testDao.getSummaryByTaskId(1));
    }

}