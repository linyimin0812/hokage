#!/bin/bash

# build

cd ./14.04 && docker build -t ubuntu-ssh:14.04 . && cd ..
cd ./16.04 && docker build -t ubuntu-ssh:16.04 . && cd ..
cd ./18.04 && docker build -t ubuntu-ssh:18.04 . && cd ..
cd ./20.04 && docker build -t ubuntu-ssh:20.04 . && cd ..

# run

docker run -d -it -p 11114:22 --name ubuntu-ssh-14.04 ubuntu-ssh:14.04
docker run -d -it -p 11116:22 --name ubuntu-ssh-16.04 ubuntu-ssh:16.04
docker run -d -it -p 11118:22 --name ubuntu-ssh-18.04 ubuntu-ssh:18.04
docker run -d -it -p 11120:22 --name ubuntu-ssh-20.04 ubuntu-ssh:20.04
