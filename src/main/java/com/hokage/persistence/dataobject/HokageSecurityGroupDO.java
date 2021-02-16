package com.hokage.persistence.dataobject;

import lombok.Data;

/**
 * @author linyimin
 * @date 2020/7/26 10:07 下午
 * @email linyimin520812@gmail.com
 * @description 安全组信息表
 */

@Data
public class HokageSecurityGroupDO extends HokageBaseDO {
    private Long id;                // 安全组id
    private Long userId;            // 安全组所属用户id
    private String servers;         // 指定服务器(IP或者分组)
    private Integer authStrategy;   // 授权策略: 0-禁止, 1-允许
    private Integer protocolType;   // 协议类型: 0-tcp, 1-udp, 2-icmp, 3-all
    private String portRange;       // 作用端口范围
    private String authObject;      // 授权对象
    private Integer status;         // 0-禁用, 1-在线
    private String description;     // 安全组描述
}
