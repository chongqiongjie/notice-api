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
COMMENT ON COLUMN public.notice_tasks.addresses IS '地址,可以多个';
COMMENT ON COLUMN public.notice_tasks.message IS '发送内容,是一个模版，利用标签和address里面的数据替换';
COMMENT ON COLUMN public.notice_tasks.status IS '发送状态';
COMMENT ON COLUMN public.notice_tasks.create_time IS '创建时间';
COMMENT ON COLUMN public.notice_tasks.update_time IS '修改时间';

create table if not exists notice_tasks_result_info(
task_id int not null,
msgid text,
address text,
status text,
is_received varchar(255),
detail_info text,
submit_time varchar(255),
send_time varchar(255),
back_time varchar(255)
);
COMMENT ON TABLE public.notice_tasks_result_info IS '发送信息任务表详细状态表';
COMMENT ON COLUMN public.notice_tasks_result_info.task_id IS '任务id';
COMMENT ON COLUMN public.notice_tasks_result_info.msgid IS '发送的信息ID';
COMMENT ON COLUMN public.notice_tasks_result_info.address IS '发送的单独的手机号/email';
COMMENT ON COLUMN public.notice_tasks_result_info.status IS '发送状态';
COMMENT ON COLUMN public.notice_tasks_result_info.is_received IS '是否触达';
COMMENT ON COLUMN public.notice_tasks_result_info.detail_info IS '详细信息';
COMMENT ON COLUMN public.notice_tasks_result_info.submit_time IS '提交到服务的时间';
COMMENT ON COLUMN public.notice_tasks_result_info.send_time IS '发送时间 ';
COMMENT ON COLUMN public.notice_tasks_result_info.back_time IS '反馈时间';

