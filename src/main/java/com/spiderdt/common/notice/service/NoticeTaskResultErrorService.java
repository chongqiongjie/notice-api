package com.spiderdt.common.notice.service;

import com.spiderdt.common.notice.dao.TasksResultDao;
import com.spiderdt.common.notice.dao.TasksResultErrorDao;
import com.spiderdt.common.notice.entity.NoticeTasksResultErrorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ranran
 * @version V1.0
 * @Title:
 * @Package com.spiderdt.common.notice.service
 * @Description:
 * @date 2017/6/29 10:29
 */
@Service
public class NoticeTaskResultErrorService {

    @Autowired
    private TasksResultDao tasksResultDao;

    @Autowired
    private TasksResultErrorDao tasksResultErrorDao;


    public void insertOneIntoResultError(String riid, NoticeTasksResultErrorEntity noticeTasksResultErrorEntity) {
        int taskId = tasksResultDao.getTaskIdByRiid(riid);
        noticeTasksResultErrorEntity.setTaskId(taskId);
        tasksResultErrorDao.insertOneIntoResultError(noticeTasksResultErrorEntity);
    }

}
