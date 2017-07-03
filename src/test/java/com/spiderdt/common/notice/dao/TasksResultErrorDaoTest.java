package com.spiderdt.common.notice.dao;

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
 * @Package com.spiderdt.common.notice.dao
 * @Description:
 * @date 2017/6/29 10:20
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class TasksResultErrorDaoTest {

    @Autowired
    TasksResultDao tasksResultDao;

    @Autowired
    TasksResultErrorDao tasksResultErrorDao;

    @Test
    public void insertOneIntoResultError() throws Exception {
        String riid = "80fbbd6a181b47f39e39d16c0296a28a";
        int taskId = tasksResultDao.getTaskIdByRiid(riid);
        System.out.println("taskId:" + taskId);
        NoticeTasksResultErrorEntity noticeTasksResultErrorEntity = new NoticeTasksResultErrorEntity(taskId, "123434", "123434", "123434", "123434");
//        noticeTasksResultErrorEntity.setTaskId(taskId);
//        noticeTasksResultErrorEntity.setAddress("123434");
//        noticeTasksResultErrorEntity.setBackTime("33dsf");
//        noticeTasksResultErrorEntity.setDetailInfo("failed");
//        noticeTasksResultErrorEntity.setSendStatus("failed");
        tasksResultErrorDao.insertOneIntoResultError(noticeTasksResultErrorEntity);
    }

}