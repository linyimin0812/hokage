FROM ubuntu:14.04

ADD aliyun-source.list /root/aliyun-sources.list

RUN \
    cp /root/aliyun-sources.list  /etc/apt/sources.list.d/source.list && \
    apt-get update && \
    apt-get install openssh-server -y && \
    apt-get install net-tools -y && \
    mkdir -p /var/run/sshd && \
    echo root:123456 | chpasswd && \
    rm -rf /etc/ssh/ssh_host_rsa_key && \
    ssh-keygen -q -t rsa -b 2048 -f /etc/ssh/ssh_host_rsa_key -P '' -N '' && \
    sed -i 's/PermitRootLogin without-password/PermitRootLogin yes/' /etc/ssh/sshd_config

EXPOSE 22

ENTRYPOINT service ssh restart && bash

# build command: docker build -t ubuntu-ssh:14.04 .