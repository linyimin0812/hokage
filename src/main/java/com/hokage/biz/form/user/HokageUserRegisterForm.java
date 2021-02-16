package com.hokage.biz.form.user;

import com.hokage.common.ExceptionInfo;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author linyimin
 * @date 2020/8/29 10:06 pm
 * @email linyimin520812@gmail.com
 * @description user register form
 */
@Data
public class HokageUserRegisterForm {

    /**
     * user id, primary key
     */
    private Long id;

    @NotNull
    @ExceptionInfo(code = "A-0001", msg = "username can't be null")
    private String username;
    @NotNull(message = "password cant be null")
    @Size(min = 6, max = 11, message = "password length must be 6-11 characters")
    @ExceptionInfo(code = "A-0002")
    private String passwd;
    /**
     * user role: 100-super operator, 1-supervisor, 2-subordinate
     */
    private Integer role;

    /**
     * user email, use to login
     */
    @NotNull(message = "email can't be null")
    @Email(message = "email is not in correct format")
    @ExceptionInfo(code = "A-0003")
    private String email;
    /**
     * subscribe: 0-not subscribe, 1: subscribe, if trgiger the condition, then send an email
     */
    private Integer subscribed;
}
