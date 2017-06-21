package com.spiderdt.common.notice.dao;

import com.spiderdt.common.notice.entity.NoticeTasksEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author ranran
 * @version V1.0
 * @Title:
 * @Package com.spiderdt.common.notice.dao
 * @Description:
 * @date 2017/6/20 11:04
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class NoticeTasksDaoTest {

    @Autowired
    NoticeTasksDao noticeTasksDao;

    @Test
    public void getNewNoticeTask() throws Exception {
    }

    @Test
    public void getNewNoticeTasks() throws Exception {
        List<NoticeTasksEntity>  noticeTasksEntities = noticeTasksDao.getNewNoticeTasks("email");
        for (NoticeTasksEntity noticeTasksEntity: noticeTasksEntities
             ) {
            System.out.println(noticeTasksEntity);
        }
    }

    @Test
    public void getNoticeTasksByStatus() throws Exception {
    }

    @Test
    public void createNoticeTask() throws Exception {
    }

    @Test
    public void updateNoticeTaskStatus() throws Exception {
    }

    @Test
    public void getAttachmentByTaskId() {
        System.out.println(noticeTasksDao.getAttachmentByTaskId(45));
//        System.out.println(noticeTasksDao.getAttachmentByTaskId(31));
//        System.out.println(noticeTasksDao.getAttachmentByTaskId(32));
//        System.out.println(noticeTasksDao.getAttachmentByTaskId(33));
    }

}