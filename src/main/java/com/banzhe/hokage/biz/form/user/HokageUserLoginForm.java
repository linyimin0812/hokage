package com.banzhe.hokage.biz.form.user;

import com.banzhe.hokage.common.ExceptionInfo;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author linyimin
 * @date 2020/8/29 10:07 下午
 * @email linyimin520812@gmail.com
 * @description 用户登录表单
 */
@Data
public class HokageUserLoginForm {
    @NotNull
    @ExceptionInfo(code = "A-0001", msg = "登录邮箱不能为空")
    private String email;        // 用户名称
    @NotNull
    @ExceptionInfo(code = "A-0002")
    private String passwd;          // 用户密码
}
