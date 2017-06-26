package com.spiderdt.common.notice.dao;

import com.spiderdt.common.notice.entity.NoticeTasksResultEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by fivebit on 2017/6/13.
 */
@Repository
public interface TasksResultDao {

    public Boolean createNoticeTaskResultBatch(List<NoticeTasksResultEntity> noticeTasksEntitys);

    /**
     * 获取结果数据表中状态为new的结果。
     * 最多返回200条结果
     * @return
     */
    public List<NoticeTasksResultEntity> getNewNoticeTaskResultsBatch(@Param("status") String status);

    /**
     * 根据sms send http返回的信息，更新result表
     * @param msgid
     * @param status
     * @param detail_info
     * @param submit_time
     * @return
     */
    public Boolean updateNoticeTaskResultStatus( @Param("msgid") String msgid,
                                                @Param("status") String status,
                                                @Param("detail_info") String detail_info,
                                                @Param("submit_time") String submit_time);

    /**
     * 更新状态、详情、发送时间、反馈时间
     * @param msgid
     * @param status
     * @param detail_info
     * @param send_time
     * @param back_time
     * @return
     */
    public Boolean updateNoticeTaskBackInfoStatus( @Param("msgid") String msgid,
                                                   @Param("status") String status,
                                                   @Param("detail_info") String detail_info,
                                                   @Param("send_time") String send_time,
                                                   @Param("back_time") String back_time);

}
