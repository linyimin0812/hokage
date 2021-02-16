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


