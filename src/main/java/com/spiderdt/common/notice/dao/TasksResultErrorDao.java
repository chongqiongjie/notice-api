package com.spiderdt.common.notice.dao;

import com.spiderdt.common.notice.entity.NoticeTasksResultErrorEntity;
import org.springframework.stereotype.Repository;

/**
 * @author ranran
 * @version V1.0
 * @Title:
 * @Package com.spiderdt.common.notice.dao
 * @Description:
 * @date 2017/6/29 10:06
 */
@Repository
public interface TasksResultErrorDao {

    /**
     * 插入一条失败记录
     * @param noticeTasksResultErrorEntity
     * @return
     */
    public boolean insertOneIntoResultError(NoticeTasksResultErrorEntity noticeTasksResultErrorEntity);

}
