package com.spiderdt.common.notice.task;

import com.spiderdt.common.notice.common.AppConstants;
import com.spiderdt.common.notice.common.Slog;
import com.spiderdt.common.notice.entity.NoticeTasksEntity;
import com.spiderdt.common.notice.entity.NoticeTasksResultEntity;
import com.spiderdt.common.notice.service.NoticeTaskService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by fivebit on 2017/6/26.
 * 把创建notcie result notice recode的逻辑放在线程中，节省时间
 */
public class DefaultCreateNoticeResultTask implements Runnable,Cloneable {
    @Autowired
    private Slog slog;

    @Autowired
    private NoticeTaskService noticeTaskService;

    public DefaultCreateNoticeResultTask(){

    }


    @Override
    public void run() {
        List<NoticeTasksEntity> notice_tasks = noticeTaskService.getInitNoticeTasks();
        slog.info("create notice task result begin:count:"+notice_tasks.size());
        if(notice_tasks != null && notice_tasks.size()>0){
            NoticeTasksEntity task = notice_tasks.get(0);
            Integer task_id = task.getTaskId();
            try {
                List<NoticeTasksResultEntity> tasksResultEntities =  noticeTaskService.makeTaskResultInfo(task_id,task);
                slog.debug("get task result entities:"+tasksResultEntities);
                noticeTaskService.saveNoticeResultsBatch(tasksResultEntities);
            }catch ( Exception ee){
                slog.error("create notice task result error:"+ee.getMessage());
                noticeTaskService.updateNoticeTaskSatus(task_id, AppConstants.TASK_STATUS_FAILED);
                return;
            }
            noticeTaskService.updateNoticeTaskSatus(task_id, AppConstants.TASK_STATUS_NEW);
        }
        slog.info("create notice task result end:");
    }
}
