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

    /**
     * 根据返回的信息，更新result表
     * @param task_id
     * @param msgid
     * @param status
     * @param detail_info
     * @param send_time
     * @param submit_time
     * @return
     */
    public Boolean updateNoticeTaskResultStatus(@Param("task_id") Integer task_id,
                                          @Param("msgid") String msgid,
                                          @Param("status") String status,
                                          @Param("detail_info") String detail_info,
                                          @Param("send_time") String send_time,
                                          @Param("submit_time") String submit_time);
    public Boolean updateNoticeTaskBackInfoStatus( @Param("msgid") String msgid,
                                                @Param("status") String status,
                                                @Param("detail_info") String detail_info,
                                                @Param("back_time") String back_time);
}
