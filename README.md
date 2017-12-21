# Alexa相关
======

关于Alexa绑定请参照: http://www.jianshu.com/p/f0c700b5a93e

AWS的Lambda服务的Python脚本可使用alexa_v3.py

git clone https://github.com/shijian-ws/alexa.git
### 进入项目目录
cd alexa/alexa-manager/
### Maven打包项目
mvn clean package
### 在git shell启用后台进程
java -jar alexa-restful-api/target/alexa-restful-api-0.1.jar &
### 初始化一个情景一个灯设备, 注意: 访问多次会添加多个
curl -X POST http://127.0.0.1:8888/api/alexa/v3/init
### 查询Alexa可控设备信息, 启动目录alexa.db, 为当前SQLite数据文件
curl http://127.0.0.1:8888/api/alexa/v3/discovery
