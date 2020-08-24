package com.banzhe.hokage.biz.dto;

import com.banzhe.hokage.common.ExceptionInfo;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author linyimin
 * @date 2020/7/26 9:40 下午
 * @email linyimin520812@gmail.com
 */

@Data
public class HokageUserDTO {
    private Long id;                // 用户id
    @NotNull
    @ExceptionInfo(code = "A-0001", msg = "用户名不能为空")
    private String username;        // 用户名称
    @NotNull(message = "用户密码不能为空")
    @Size(min = 6, max = 11, message = "密码长度必须是6-16个字符")
    @ExceptionInfo(code = "A-0021")
    private String passwd;          // 用户密码
    private Integer role;           // 用户角色： 0: 超级管理员, 1 管理员, 2普通用户
    @Email(message = "邮箱格式不正确")
    @ExceptionInfo(code = "A-0031")
    private String email;           // 用户邮箱
    private Integer isSubscribed;   // 是否订阅, 0: 不订阅, 1: 订阅, 发送消息邮件
}