package com.hokage.biz.form.user;

import com.hokage.common.ExceptionInfo;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author linyimin
 * @date 2020/10/26 12:23 am
 * @email linyimin520812@gmail.com
 * @description user logout form
 */
@Data
public class HokageUserLogoutForm {
    @NotNull
    @ExceptionInfo(code = "A-0001", msg = "user email can't be null")
    private String email;
}
