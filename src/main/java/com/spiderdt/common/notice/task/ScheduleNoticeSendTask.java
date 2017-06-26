package com.spiderdt.common.notice.task;

import com.spiderdt.common.notice.common.Slog;
import com.spiderdt.common.notice.service.NoticeTaskService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by fivebit on 2017/6/26.
 * 例行的任务，每隔30秒，查找数据库中需要发送的notice task。进行发送。
 */
public class ScheduleNoticeSendTask implements Runnable {

    @Autowired
    private Slog slog;
    @Autowired
    private NoticeTaskService noticeTaskService;

    @Override
    public void run() {
        slog.info("ScheduleNoticeSendTask begin");
        try {
            noticeTaskService.sendNoticeTaskByDbScan();
        }catch (Exception ee){
            slog.error("send notice by db scan task error:"+ee.getMessage());
        }
        slog.info("ScheduleNoticeSendTask end");
    }
}
