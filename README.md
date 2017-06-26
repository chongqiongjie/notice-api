## notice api

### 打包

系统使用maven构建，执行mvn clean install -P[dev,test,prod], 在target目录下，会生成开发包，测试包，线上包 chain-api.war。

mvn clean install  -Dmaven.test.skip=true -Ptest


### TODO
- [ ] 任务列表，前端点击显示任务结果具体情况