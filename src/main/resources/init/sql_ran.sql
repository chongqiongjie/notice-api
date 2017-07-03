#table schema

DROP TABLE notice.notice_tasks_result_error_info;
create table if not exists notice.notice_tasks_result_error_info(
    task_id int not null,
    riid text,
    address text,
    send_status text,
    detail_info text,
    back_time varchar(255)
);
COMMENT ON TABLE notice.notice_tasks_result_error_info IS '发送信息任务表错误详细状态表';
COMMENT ON COLUMN notice.notice_tasks_result_error_info.task_id IS '任务id';
COMMENT ON COLUMN notice.notice_tasks_result_error_info.address IS '发送的单独的手机号/email';
COMMENT ON COLUMN notice.notice_tasks_track_recode.riid IS '所属任务的riid';
COMMENT ON COLUMN notice.notice_tasks_result_error_info.send_status IS '发送状态';
COMMENT ON COLUMN notice.notice_tasks_result_error_info.detail_info IS '详细信息包括错误信息等';
COMMENT ON COLUMN notice.notice_tasks_result_error_info.back_time IS '反馈时间';

INSERT INTO notice.notice_tasks_result_error_info (riid, address, send_status, detail_info, back_time)
    'riid', 'fsdf', 'fsdf', 'dfsd', 'fsdf';

INSERT INTO notice.notice_tasks_result_error_info (task_id)
    SELECT task_id FROM notice.notice_tasks_result_info WHERE riid = 'fdf';


INSERT INTO notice.notice_tasks_track_recode VALUES ('track_url_suffix_test', 'click', 'url_org_test', 3000, 'riid_test', 'message_replace_test', 'message_org', '', 0, '未查看');
INSERT INTO notice.notice_tasks_track_recode VALUES ('123456', 'click', 'url_org_test', 3000, 'riid_test1', 'message_replace_test', 'message_org', '', 0, '未查看');
INSERT INTO notice.notice_tasks_track_recode VALUES ('open123', 'open', 'url_org_test', 3000, 'riid_test1', 'message_replace_test', 'message_org', '', 0, 'clickTime');

UPDATE notice.notice_tasks_summary
    SET click_count = click_count+1
WHERE task_id = 1;

SELECT task_id, notice_count, sent_count, view_count, click_count
FROM notice.notice_tasks_summary WHERE task_id =1;

SELECT COUNT(*) FROM notice.notice_tasks_result_info
    WHERE (send_status = 'new' OR send_status = 'authFailed')
    AND task_id = 102;

