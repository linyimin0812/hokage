package com.banzhe.hokage.persistence.dataobject;

import lombok.Data;

import java.util.Date;

/**
 * @author linyimin
 * @date 2020/7/26 9:40 下午
 * @email linyimin520812@gmail.com
 */

@Data
public class HokageUserDO extends HokageBaseDO {
    private Long id;                // 用户id
    private String name;            // 用户名称
    private String passwd;          // 用户密码
    private Integer role;           // 用户角色： 0: 超级管理员, 1 管理员, 2普通用户
    private String email;           // 用户邮箱
    private Integer isSubscribed;   // 是否订阅, 0: 不订阅, 1: 订阅, 发送消息邮件
}