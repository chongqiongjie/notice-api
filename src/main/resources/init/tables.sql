#table schema

create table if not exists notice_tasks(
task_id SERIAL primary key not null,
parent_task_id int,
task_type varchar(255),
client_id varchar(255),
user_id varchar(255),
addresses text,
message text,
status text,
create_time varchar(255),
update_time varchar(255)
);
COMMENT ON TABLE public.notice_tasks IS '发送信息任务表';
COMMENT ON COLUMN public.notice_tasks.task_id IS '任务ID';
COMMENT ON COLUMN public.notice_tasks.parent_task_id IS '父任务';
COMMENT ON COLUMN public.notice_tasks.task_type IS '任务类型：sms/email';
COMMENT ON COLUMN public.notice_tasks.client_id IS '客户端ID';
COMMENT ON COLUMN public.notice_tasks.user_id IS '任务发起人';
COMMENT ON COLUMN public.notice_tasks.addresses IS '地址,可以多个,是一个json list格式。';
COMMENT ON COLUMN public.notice_tasks.message IS '发送内容,是一个模版，利用标签和address里面的数据替换';
COMMENT ON COLUMN public.notice_tasks.status IS '发送状态';
COMMENT ON COLUMN public.notice_tasks.create_time IS '创建时间';
COMMENT ON COLUMN public.notice_tasks.update_time IS '修改时间';

create table if not exists notice_tasks_result_info(
rid SERIAL primary key not null,
task_id int not null,
msgid text,
track_url_suffix text,
address text,
message text,
send_status text,
detail_info text,
is_open varchar(255),
is_click varchar(255),
open_time varchar(255),
click_time varchar(255),
submit_time varchar(255),
send_time varchar(255),
back_time varchar(255)
);
COMMENT ON TABLE public.notice_tasks_result_info IS '发送信息任务表详细状态表';
COMMENT ON COLUMN public.notice_tasks_result_info.task_id IS '任务id';
COMMENT ON COLUMN public.notice_tasks_result_info.msgid IS '发送的信息ID';
COMMENT ON COLUMN public.notice_tasks_result_info.track_url_suffix IS '发送的模版里面的链接的后缀部分';
COMMENT ON COLUMN public.notice_tasks_result_info.address IS '发送的单独的手机号/email';
COMMENT ON COLUMN public.notice_tasks_result_info.message IS '发送的最终信息';
COMMENT ON COLUMN public.notice_tasks_result_info.send_status IS '发送状态';
COMMENT ON COLUMN public.notice_tasks_result_info.detail_info IS '详细信息包括错误信息等';
COMMENT ON COLUMN public.notice_tasks_result_info.is_open IS '是否打开，主要在邮件中';
COMMENT ON COLUMN public.notice_tasks_result_info.is_click IS '是否点击';
COMMENT ON COLUMN public.notice_tasks_result_info.open_time IS '打开邮件时间';
COMMENT ON COLUMN public.notice_tasks_result_info.click_time IS '点击track url 时间';
COMMENT ON COLUMN public.notice_tasks_result_info.submit_time IS '提交到服务的时间';
COMMENT ON COLUMN public.notice_tasks_result_info.send_time IS '发送时间 ';
COMMENT ON COLUMN public.notice_tasks_result_info.back_time IS '反馈时间';

create table if not exists notice_tasks_track_recode (
track_url_suffix text,
url_org text,
task_id int not null,
message_replace text,
message_org text,
params text,
status int
);
COMMENT ON TABLE public.notice_tasks_track_recode IS 'url加密对以及记录该加密url的一些来源等属性';
COMMENT ON COLUMN public.notice_tasks_track_recode.track_url_suffix IS 'track url的后缀,是md5 url_org和params加密之后的字符串';
COMMENT ON COLUMN public.notice_tasks_track_recode.url_org IS '发送消息中原始的url'
COMMENT ON COLUMN public.notice_tasks_track_recode.task_id IS '所属任务的task_id';
COMMENT ON COLUMN public.notice_tasks_track_recode.message_replace IS '替换track url之后的message';
COMMENT ON COLUMN public.notice_tasks_track_recode.message_org IS '替换track url之前的message'
COMMENT ON COLUMN public.notice_tasks_track_recode.params IS '一些属性，Json字符串，该字段和org字段结合生成加密字段'
COMMENT ON COLUMN public.notice_tasks_track_recode.status IS '状态，0／1'
