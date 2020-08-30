package com.banzhe.hokage.controller;

import com.banzhe.hokage.biz.form.user.HokageUserLoginForm;
import com.banzhe.hokage.biz.form.user.HokageUserRegisterForm;
import com.banzhe.hokage.biz.service.HokageUserService;
import com.banzhe.hokage.common.BaseController;
import com.banzhe.hokage.common.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @author linyimin
 * @date 2020/8/23 1:32 上午
 * @email linyimin520812@gmail.com
 * @description 处理用户对应的controller
 */
@RestController
public class UserController extends BaseController {

    private HokageUserService userService;

    public void setUserService(HokageUserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    public ResultVO<HokageUserRegisterForm> register(@RequestBody @Valid HokageUserRegisterForm userForm, HttpSession session) {

        // 先判断用户名是否已经存在



        return success(userForm);
    }

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public ResultVO<HokageUserLoginForm> login(@RequestBody @Valid HokageUserLoginForm userForm) {
        return success(userForm);
    }

    @RequestMapping(value = "/user/modify", method = RequestMethod.POST)
    public ResultVO<HokageUserRegisterForm> modify(@RequestBody @Valid HokageUserRegisterForm userForm) {
        return success(userForm);
    }
}
