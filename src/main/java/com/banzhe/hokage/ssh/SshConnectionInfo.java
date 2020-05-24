package com.banzhe.hokage.ssh;

import lombok.Data;

@Data
public class SshConnectionInfo {
    private String host;        // ssh IP
    private int port;           // ssh 端口
    private String username;    // ssh登录账号
    private String passwd;      // 登录密码
}
