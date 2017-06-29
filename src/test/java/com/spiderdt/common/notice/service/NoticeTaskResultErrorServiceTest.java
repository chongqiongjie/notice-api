package com.spiderdt.common.notice.service;

import com.spiderdt.common.notice.entity.NoticeTasksResultErrorEntity;
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
 * @date 2017/6/29 10:36
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class NoticeTaskResultErrorServiceTest {

    @Autowired
    private NoticeTaskResultErrorService noticeTaskResultErrorService;

    @Test
    public void insertOneIntoResultError() throws Exception {

        NoticeTasksResultErrorEntity noticeTasksResultErrorEntity = new NoticeTasksResultErrorEntity(0, "wer","wer","wer","wer");
        noticeTaskResultErrorService.insertOneIntoResultError("0edaaf72009d45c7be4de1aff9410824", noticeTasksResultErrorEntity);
    }

}