#table schema

INSERT INTO notice.notice_tasks_track_recode VALUES ('track_url_suffix_test', 'click', 'url_org_test', 3000, 'riid_test', 'message_replace_test', 'message_org', '', 0, '未查看');
INSERT INTO notice.notice_tasks_track_recode VALUES ('123456', 'click', 'url_org_test', 3000, 'riid_test1', 'message_replace_test', 'message_org', '', 0, '未查看');
INSERT INTO notice.notice_tasks_track_recode VALUES ('open123', 'open', 'url_org_test', 3000, 'riid_test1', 'message_replace_test', 'message_org', '', 0, 'clickTime');

UPDATE notice.notice_tasks_summary
    SET click_count = click_count+1
WHERE task_id = 1;

SELECT task_id, notice_count, sent_count, view_count, click_count FROM notice.notice_tasks_summary WHERE task_id =1;