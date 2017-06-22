package com.spiderdt.common.notice.dao;

import com.spiderdt.common.notice.entity.TestEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface TestDao {

    public TestEntity getSummaryByTaskId(@Param("taskId") int taskId);

}
