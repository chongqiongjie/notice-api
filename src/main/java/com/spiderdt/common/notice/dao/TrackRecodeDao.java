package com.spiderdt.common.notice.dao;

import com.spiderdt.common.notice.entity.TrackRecodeEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by fivebit on 2017/6/3.
 * 对应notice_tasks_track_recode 这张表。
 */
@Repository
public interface TrackRecodeDao {
    /**
     * 根据加密url的key，查询数据库中的记录
     * @param encrypt_url
     * @return
     */
    public TrackRecodeEntity getTrackRecodeInfoByEncrypt(@Param("encrypt_url") String encrypt_url);

    /**
     * 批量创建 加密url的详细信息
     * @param trackRecodeEntities
     * @return
     */
    public Boolean addTrackRecodeInfoBatch(List<TrackRecodeEntity> trackRecodeEntities);

    public Boolean updateTrackRecodeStatus(@Param("encrypt_url") String encrypt_url, @Param("status") int status, @Param("clickTime") String clickTime);

}

