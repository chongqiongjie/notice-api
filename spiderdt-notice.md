##邮件／短信发送模块 功能点

### web
1. 提供默认和个性化的邮件／短信模版选择框。
2. 提供创建个性化邮件／短信模版。
3. 为邮件提供富文本编辑器。
4. 为邮件／短信提供wildcard，例如#name# ，只可添加／删除，不可修改。
5. 关联jobid，创建发送邮件／短信task。
6. task 列表，有taskname，创建时间，task状态，发送总条数，成功率，删除按钮，（失败重发）。
7. 提供task的详情，页面里提供分页显示各条通知地址／发送时间／发送状态／是否查看／是否点击链接／错误原因。

### RestFul api
需要提供的接口功能：

1. 按照用户获取模版列表，按照个性化，默认，创建时间倒序排列。
2. 创建模版接口。
3. 创建发送通知task。通过jobId，获取address。
4. 获取task列表，分页展示。
5. 删除task接口。
6. 按照task_id,获取task里面的详情情况。分页返回。

### 服务／基础功能
1. deamon进程，轮询扫库。对邮件／短信的新建任务，放到各自的线程池中。
2. 短信API对接。对接三通的http api。
3. 邮件API，使用spiderdt.com 腾讯提供的服务。
4. 短链接api对接。目前考察了t.cn／bit.ly/ 都是通过oauth2接入。但是都有limit。短链的原url是spiderdt.com服务，再进行跳转。
5. 邮件内容生成，包括打开监控／添加短链等。 
6. 提供track serv。
7. 邮件上传图片，文件服务=>文件数据库
8. 文件服务，对外提供url。


### 数据库设计

发送任务表

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
任务详细结果表	

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
原URL和跟踪URL pair 对。

	create table if not exists notice_tasks_track_recode (
	url_encode text,
	url_org text,
	params text,
	status int
	);
### 部分约定