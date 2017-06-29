#table schema

CREATE SCHEMA notice;
DROP SCHEMA notice;

create table if not exists notice.notice_tasks(
task_id SERIAL primary key not null,
parent_task_id int,
task_type varchar(255),
client_id varchar(255),
user_id varchar(255),
job_id text,
template_id int,
addresses text,
message text,
subject text,
attachments text,
status text,
create_time varchar(255),
update_time varchar(255)
);
create index idx_notice_notice_tasks_user_id_status on notice.notice_tasks (user_id,status);
create index idx_notice_notice_tasks_status on notice.notice_tasks (status);
COMMENT ON TABLE notice.notice_tasks IS '发送信息任务表';
COMMENT ON COLUMN notice.notice_tasks.task_id IS '任务ID';
COMMENT ON COLUMN notice.notice_tasks.parent_task_id IS '父任务';
COMMENT ON COLUMN notice.notice_tasks.task_type IS '任务类型：sms/email';
COMMENT ON COLUMN notice.notice_tasks.client_id IS '客户端ID';
COMMENT ON COLUMN notice.notice_tasks.user_id IS '任务发起人';
COMMENT ON COLUMN notice.notice_tasks.job_id IS '关联的jobid';
COMMENT ON COLUMN notice.notice_tasks.template_id IS '模板ID';
COMMENT ON COLUMN notice.notice_tasks.addresses IS '地址,可以多个,是一个json list格式eg[phone:xxx,name:xxx] or [email:xxx,name:xxxx]';
COMMENT ON COLUMN notice.notice_tasks.message IS '发送内容,是一个模版，利用标签和address里面的数据替换';
COMMENT ON COLUMN notice.notice_tasks.subject IS 'email 发送的subject';
COMMENT ON COLUMN notice.notice_tasks.attachments IS 'email 发送的attachments';
COMMENT ON COLUMN notice.notice_tasks.status IS '发送状态';
COMMENT ON COLUMN notice.notice_tasks.create_time IS '创建时间';
COMMENT ON COLUMN notice.notice_tasks.update_time IS '修改时间';

create table if not exists notice.notice_tasks_summary(
task_id  int,
notice_count int,
sent_count int,
view_count int,
click_count int
);
COMMENT ON TABLE notice.notice_tasks_summary IS 'task级别的统计信息';
COMMENT ON COLUMN notice.notice_tasks_summary.task_id IS '任务ID';
COMMENT ON COLUMN notice.notice_tasks_summary.notice_count IS '总共需要发送的通知数目';
COMMENT ON COLUMN notice.notice_tasks_summary.sent_count IS '发送成功的数量';
COMMENT ON COLUMN notice.notice_tasks_summary.view_count IS '查看的数量';
COMMENT ON COLUMN notice.notice_tasks_summary.click_count IS '点击的数量';

create table if not exists notice.notice_tasks_result_info(
riid text primary key not null,
task_id int not null,
address text,
message text,
subject text,
send_status text,
detail_info text,
submit_time varchar(255),
send_time varchar(255),
back_time varchar(255)
);
create index idx_notice_notice_tasks_result_info_task_id on notice.notice_tasks_result_info (task_id);
create index idx_notice_notice_tasks_result_info_send_status on notice.notice_tasks_result_info (send_status);
COMMENT ON TABLE notice.notice_tasks_result_info IS '发送信息任务表详细状态表';
COMMENT ON COLUMN notice.notice_tasks_result_info.riid IS 'UUID,可作为发送的信息ID';
COMMENT ON COLUMN notice.notice_tasks_result_info.task_id IS '任务id';
COMMENT ON COLUMN notice.notice_tasks_result_info.address IS '发送的单独的手机号/email';
COMMENT ON COLUMN notice.notice_tasks_result_info.subject IS 'email时发送的subject,sms时为空';
COMMENT ON COLUMN notice.notice_tasks_result_info.message IS '发送的最终信息';
COMMENT ON COLUMN notice.notice_tasks_result_info.send_status IS '发送状态';
COMMENT ON COLUMN notice.notice_tasks_result_info.detail_info IS '详细信息包括错误信息等';
COMMENT ON COLUMN notice.notice_tasks_result_info.submit_time IS '提交到服务的时间';
COMMENT ON COLUMN notice.notice_tasks_result_info.send_time IS '发送时间 ';
COMMENT ON COLUMN notice.notice_tasks_result_info.back_time IS '反馈时间';

create table if not exists notice.notice_tasks_track_recode (
track_url_suffix text primary key not null,
track_type text,
url_org text,
task_id int not null,
riid text not null,
message_replace text,
message_org text,
params text,
is_click int,
click_time varchar(255)
);
create index idx_notice_notice_tasks_track_recode_task_id on notice.notice_tasks_track_recode (task_id);
COMMENT ON TABLE notice.notice_tasks_track_recode IS 'url加密对以及记录该加密url的一些来源等属性';
COMMENT ON COLUMN notice.notice_tasks_track_recode.track_url_suffix IS 'track url的后缀,是md5 url_org和params加密之后的字符串';
COMMENT ON COLUMN notice.notice_tasks_track_recode.track_type IS 'track url type eg:click/open ';
COMMENT ON COLUMN notice.notice_tasks_track_recode.url_org IS '发送消息中原始的url';
COMMENT ON COLUMN notice.notice_tasks_track_recode.task_id IS '所属任务的task_id';
COMMENT ON COLUMN notice.notice_tasks_track_recode.riid IS '所属任务的riid';
COMMENT ON COLUMN notice.notice_tasks_track_recode.message_replace IS '替换track url之后的message';
COMMENT ON COLUMN notice.notice_tasks_track_recode.message_org IS '替换track url之前的message';
COMMENT ON COLUMN notice.notice_tasks_track_recode.params IS '一些属性，Json字符串，该字段和org字段结合生成加密字段';
COMMENT ON COLUMN notice.notice_tasks_track_recode.is_click IS '是否点击/查看,1/0';
COMMENT ON COLUMN notice.notice_tasks_track_recode.click_time IS '点击/查看时间';

create table if not exists notice.notice_message_template (
tid SERIAL primary key not null,
temp_name text,
message_type text,
temp_type text,
user_id text,
template_subject text,
template_content text,
create_time text,
update_time text,
is_valid int
);
COMMENT ON TABLE notice.notice_message_template IS '消息模版表';
COMMENT ON COLUMN notice.notice_message_template.tid IS '主键自增ID';
COMMENT ON COLUMN notice.notice_message_template.temp_name IS '模版名称';
COMMENT ON COLUMN notice.notice_message_template.message_type IS '消息类型sms/email';
COMMENT ON COLUMN notice.notice_message_template.temp_type IS '模版类型，notice／private';
COMMENT ON COLUMN notice.notice_message_template.user_id IS '所属用户';
COMMENT ON COLUMN notice.notice_message_template.template_subject IS '消息的头部，用于email';
COMMENT ON COLUMN notice.notice_message_template.template_content IS '消息的内容，用于sms 和email';
COMMENT ON COLUMN notice.notice_message_template.create_time IS '创建时间';
COMMENT ON COLUMN notice.notice_message_template.update_time IS '修改时间';
COMMENT ON COLUMN notice.notice_message_template.is_valid IS '状态0/1'


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