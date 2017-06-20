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
 * @date 2017/6/20 15:07
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class FileServiceTest {

    @Autowired
    FileService fileService;

    @Test
    public void download(){
        fileService.download("http://www.qqleju.com/uploads/allimg/130618/18-110930_9.jpg", "ran","chong.jpg");
    }

    @Test
    public void testDeleteFile() {
        fileService.deleteFile("E:\\tmp\\ran\\ran.jpg");
    }

    @Test
    public void testDeleteDirectory() {
        fileService.deleteDirectory("E:\\tmp\\ran");
    }

}