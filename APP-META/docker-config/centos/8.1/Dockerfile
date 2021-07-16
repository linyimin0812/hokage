FROM centos:8.1.1911

RUN \
    yum update -y && \
    yum install openssl openssh-server net-tools -y

RUN \
    echo root:123456 | chpasswd && \
    ssh-keygen -q -t rsa -b 2048 -f /etc/ssh/ssh_host_rsa_key -N '' && \
    ssh-keygen -q -t ecdsa -f /etc/ssh/ssh_host_ecdsa_key -N '' && \
    ssh-keygen -t dsa -f /etc/ssh/ssh_host_ed25519_key -N '' && \
    sed -i 's/PermitRootLogin without-password/PermitRootLogin yes/' /etc/ssh/sshd_config && \
    sed -i 's/PermitRootLogin prohibit-password/PermitRootLogin yes/' /etc/ssh/sshd_config && \
    sed -i 's/#UseDNS yes/UesDNS no/' /etc/ssh/sshd_config && \
    sed -i 's/GSSAAPIAuthentication yes/GSSAAPIAuthentication no/' /etc/ssh/sshd_config && \
    echo "PermitRootLogin yes" >> /etc/ssh/sshd_config

EXPOSE 22

ENTRYPOINT /usr/sbin/sshd -D && bash