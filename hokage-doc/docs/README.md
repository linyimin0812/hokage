---
home: true
actionText: 快速上手 →
actionLink: /zh/guide/
features:
- title: 用户管理
  details: 简单易用的服务器用户管理, 支持超级管理员, 管理员, 普通用户三种角色, 一键添加, 删除服务器用户
- title: 服务器集群管理
  details: 支持Web终端, 文件管理, 批量任务, 安全组, 资源监控, 让您随时了解服务器状态, 快速操作服务器
- title: IP自动上报
  details: 服务器IP地址变化时, 自动上报绑定域名, 无须感知IP变化
footer: Server Management ©2020 Created by github@linyimin-bupt
---

## 启动

```shell script
mvn clean package -Ddockerfile.build.skip=true
cd target
./hokage-0.0.1-SNAPSHOT
```

## Docker启动

```shell script
mvn clean package
docker run -d --restart=always --name hokage -p 8080:8080 hokage-0.0.1:SNAPSHOT
```