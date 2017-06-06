package com.spiderdt.common.notice.service;

import com.spiderdt.common.notice.common.Jlog;
import com.spiderdt.common.notice.dao.NoticeTasksDao;
import com.spiderdt.common.notice.entity.NoticeTasksEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by fivebit on 2017/6/5.
 */
@SuppressWarnings("ALL")
@Service("noticeTaskService")
public class NoticeTaskService {
    @Resource
    private NoticeTasksDao noticeTasksDao;

    public Boolean createNoticTask(NoticeTasksEntity noticeTasksEntity){
        try {
            noticeTasksDao.createNoticeTask(noticeTasksEntity);
        }catch (Exception ee){
            Jlog.error("create notice task error:"+ee.getMessage());
        }
        return true;
    }
}
