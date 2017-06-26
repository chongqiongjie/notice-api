## notice api

### 框架
spring+jersey+mybatis+postgresql

### 打包

系统使用maven构建，执行mvn clean install -P[dev,test,prod], 在target目录下，会生成开发包，测试包，线上包 notice-api.war 和 notice-api.jar

### TODO
- [ ] 任务列表，前端点击显示任务结果具体情况

### 接口说明：

创建任务：

POST http://192.168.1.2:8095/notice-api/task
body:
{"task_type":"sms","client_id":"jupiter","user_id":"tutuanna","job_id":"xxx",
"template_id":10,"message":"6月9日运动户外低至五折"}
{"task_type":"email","client_id":"jupiter","user_id":"tutuanna","job_id":"xxx",
"template_id":10,"message":"6月9日运动户外低至五折","subject":"xxx","attachments":""}
