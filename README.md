## 项目描述

本项目旨在为IP多变的小集群提供管理能力。主要能力包含用户管理、服务器管理、账号管理、Web终端、文件管理、批量任务、资源监控及IP主动上报等。

 用户管理 
  
用户分为3个角色：超级管理员，管理员及普通用户.
 
超级管理员 
    负责服务器的添加，修改，删除及管理员的指定和撤销等。
管理员
  负责服务器的具体管理——给普通用户添加账号，目前一个普通用户支持在在一台服务器创建一个账号（系统用户名+主键id），回收账号等
普通用户



## 开发环境

1. node: 12+
2. java 8+

## 启动

```shell script
docker run -itd --restart=always --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 mysql:5.6
```

### use the fat jar to start

```shell script
mvn clean package -Ddockerfile.build.skip=true
cd target
./hokage-0.0.1-SNAPSHOT
```

### use docker to start

```shell script
mvn clean package
docker run -d --restart=always --name hokage -p 8080:8080 hokage-0.0.1:SNAPSHOT
```


## swagger

```
127.0.0.1:8080/swagger-ui/index.html?url=/v3/api-docs
```

## development

### set proxy port

houkage-ui/package.json

```json
{
  "proxy": "http://127.0.0.1:8080"
}
```

### start up ui

```shell script
npm install
PORT=3000 npm start
```


