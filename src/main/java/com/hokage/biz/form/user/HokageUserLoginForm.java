package com.hokage.biz.form.user;

import com.hokage.common.ExceptionInfo;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author linyimin
 * @date 2020/8/29 10:07 pm
 * @email linyimin520812@gmail.com
 * @description user login form
 */
@Data
public class HokageUserLoginForm {
    @NotNull
    @ExceptionInfo(code = "A-0001", msg = "user email can't be null")
    private String email;
    @NotNull
    @ExceptionInfo(code = "A-0002")
    private String passwd;
}
