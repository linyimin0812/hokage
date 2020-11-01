package com.banzhe.hokage.biz.form.server;

import lombok.Data;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/11/1 5:51 下午
 * @email linyimin520812@gmail.com
 * @description 服务器信息表单
 */
@Data
public class HokageServerForm {
    private String domain;              // 域名
    private String ip;                  // 服务器ip
    private String sshPort;             // ssh端口
    private String account;             // 登录账号
    private String passwd;              // 登录密码
    private String serverGroup;         // 服务器分组
    private List<Long> supervisors;     // 服务器管理员
}
