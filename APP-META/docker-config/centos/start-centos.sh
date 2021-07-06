#!/bin/bash

# build

cd ./7.1 && docker build -t centos-ssh:7.1 . && cd ..
cd ./7.2 && docker build -t centos-ssh:7.2 . && cd ..
cd ./7.5 && docker build -t centos-ssh:7.5 . && cd ..
cd ./7.6 && docker build -t centos-ssh:7.6 . && cd ..
cd ./8.1 && docker build -t centos-ssh:8.1 . && cd ..
cd ./8.2 && docker build -t centos-ssh:8.2 . && cd ..

# run

docker run -d -it -p 11171:22 --name centos-ssh:7.1 centos-ssh:7.1
docker run -d -it -p 11172:22 --name centos-ssh:7.1 centos-ssh:7.2
docker run -d -it -p 11175:22 --name centos-ssh:7.5 centos-ssh:7.5
docker run -d -it -p 11176:22 --name centos-ssh:7.6 centos-ssh:7.6
docker run -d -it -p 11181:22 --name centos-ssh:8.1 centos-ssh:7.5
docker run -d -it -p 11182:22 --name centos-ssh:8.2 centos-ssh:7.6
