package com.spiderdt.common.notice.dao;

import com.spiderdt.common.notice.entity.NoticeTasksEntity;
import com.spiderdt.common.notice.entity.NoticeTasksResultEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by fivebit on 2017/5/19.
 */
public interface NoticeTasksDao {
    /**
     * 获取一个新创建的task
     * @return
     */
    public NoticeTasksEntity getNewNoticeTask(@Param("task_type") String task_type);

    /**
     * 获取新创建的task list
     * @return
     */
    public List<NoticeTasksEntity> getNewNoticeTasks(@Param("task_type") String task_type);

    /**
     * 获取特定状态的task的list
     * @param status
     * @return
     */
    public List<NoticeTasksEntity> getNoticeTasksByStatus(@Param("status") String status);

    /**
     * 创建一个新的task
     * @param noticeTasksEntity
     * @return
     */
    public Integer createNoticeTask(NoticeTasksEntity noticeTasksEntity);
    public void createNoticeTaskResultBatch(List<NoticeTasksResultEntity> noticeTasksEntitys);

    /**
     * 更新任务的状态
     * @param task_id
     * @param status
     * @return
     */
    public Boolean updateNoticeTaskStatus(@Param("task_id") Integer task_id,
                                          @Param("status") String status,
                                          @Param("update_time") String update_time);
}
