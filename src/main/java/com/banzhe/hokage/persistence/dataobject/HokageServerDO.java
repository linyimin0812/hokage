package com.banzhe.hokage.persistence.dataobject;

import lombok.Data;

/**
 * @author linyimin
 * @date 2020/7/26 10:12 下午
 * @email linyimin520812@gmail.com
 * @description 服务器信息
 */

@Data
public class HokageServerDO extends HokageBaseDO {
    private Long id;            // 服务器id
    private String hostname;    // 服务器主机名
    private String domain;      // 服务器对应域名
    private String ip;          // 服务器ip
    private String sshPort;     // ssh端口
    private String account;     // ssh远程登录账号
    private String passwd;      // ssh远程登录密码
    private String serverGroup;       // 服务器所属组
    private Integer type;        // 服务器类型, 内网、外网、X86、GPU等
    private String description; // 服务器描述信息
}
