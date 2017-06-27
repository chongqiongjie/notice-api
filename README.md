## notice api

### 框架
spring+jersey+mybatis+postgresql

### 打包

系统使用maven构建，执行mvn clean install -P[dev,test,prod], 在target目录下，会生成开发包，测试包，线上包 notice-api.war。

跳过单测，使用：mvn clean install  -Dmaven.test.skip=true -Ptest

### 系统流程
#### 创建流程
1. 调用createTask api。使用task_type=sms/email创建任务，通过job_id,获取对应的用户信息，初始化address字段，task status初始化为`init`。
2. 对于email，address字段一般为一个，不需要失败取下一个。对于phone的情况。服务提供了前20个手机号，按照score降序排列。初始化时，默认取第一个phone插入数据库。其余存放在redis中，key为riid。
3. 如果是邮件，则下载对应的附件。通过task_id命名的文件夹下。
4. createTask api 接口返回 status: 200。
5. `DefaultCreateNoticeResultTask` 初始化notice 任务的线程。每隔10秒中，扫描一次数据表notice_tasks的status字段，值为init的task。进行初始化，最后设置task status= new。
6. message中的link，通过变换，生成trackid，包括a标签的点击url和查看email等。存放在notice_tasks_track_recode表中。
7. 对于每个address，生成一条notice_tasks_result_info，send_status为new的记录。该记录的message的link已经替换成对应的跟踪链接。
8. `ScheduleNoticeSendTask` 发送notice的线程。每隔45秒，扫描一次数据表notice_tasks_result_info的send_status字段为new和auth_failed的记录。（这里可以添加上扫描发送失败的短信地址，更新成下一个优先级的phone，初始状态为new的记录。）判断task_type。分别调用sms和email进行发送。
9. 更新记录对应的task状态为sending。更新对应记录的send_status为sending。
10. sms发送时，通过DefaultSmsSendTask线程，调用三通的服务接口。根据对应的返回，更新发送状态。
11. email发送时，调用腾讯的邮件服务，根据返回结果，更新发送状态。
12. 对于sms发送，是否送达的状态是异步的。通过`ScheduleSyncSmsSendStatusTask` 线程，每隔30秒，请求一次三通的服务，获取之前短信的状态（该状态只返回一次，下次不再返回，每批最多200条记录。）。根据返回的code，更新发送状态。
13. 用户点击url，会转发到spider的track服务api。根据track_url_suffix，取redis或者db中的数据，进行log和修改状态。最终302跳转到目标url。


### TODO

- [ ] 任务列表，前端点击显示任务结果具体情况
- [ ] 任务发送结束状态的更新。目前还缺少一个线程，循环查找处于sending状态的任务的发送结果。进行更新。
- [ ] 对于短信发送失败的情况，需要获取下一个可用phone，进行发送。
- [ ] 建立不可用phone的列表，便于初始化时候进行过滤，也创建一个可用phone，给后端优化socre排名。
- [ ] 对于sms的短链接，bit服务有时比较慢，需要调研一下t.cn的服务。

### 接口说明：

创建任务：

	POST http://192.168.1.2:8095/notice-api/task
	body:
	{"task_type":"sms","client_id":"jupiter","user_id":"tutuanna","job_id":"xxx",
	"template_id":10,"message":"6月9日运动户外低至五折"}
	或者	
	{"task_type":"email","client_id":"jupiter","user_id":"tutuanna","job_id":"xxx",
	"template_id":10,"message":"6月9日运动户外低至五折","subject":"xxx","attachments":""}





### 数据库表
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
	