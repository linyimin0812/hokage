package com.hokage.biz.form.user;

import com.hokage.common.ExceptionInfo;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author linyimin
 * @date 2020/10/26 12:23 上午
 * @email linyimin520812@gmail.com
 * @description 用户登出表单
 */
@Data
public class HokageUserLogoutForm {
    @NotNull
    @ExceptionInfo(code = "A-0001", msg = "邮箱不能为空")
    private String email;
}
