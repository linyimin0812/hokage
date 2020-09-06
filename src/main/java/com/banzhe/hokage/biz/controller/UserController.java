package com.banzhe.hokage.biz.controller;

import com.alibaba.fastjson.JSON;
import com.banzhe.hokage.biz.Constant;
import com.banzhe.hokage.biz.converter.UserConverter;
import com.banzhe.hokage.biz.form.user.HokageUserLoginForm;
import com.banzhe.hokage.biz.form.user.HokageUserRegisterForm;
import com.banzhe.hokage.biz.service.HokageUserService;
import com.banzhe.hokage.common.BaseController;
import com.banzhe.hokage.common.ResultVO;
import com.banzhe.hokage.common.ServiceResponse;
import com.banzhe.hokage.persistence.dataobject.HokageUserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Objects;

/**
 * @author linyimin
 * @date 2020/8/23 1:32 上午
 * @email linyimin520812@gmail.com
 * @description 处理用户对应的controller
 */
@RestController
public class UserController extends BaseController {

    private HokageUserService userService;

    @Autowired
    public void setUserService(HokageUserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    public ResultVO<HokageUserRegisterForm> register(@RequestBody @Valid HokageUserRegisterForm userForm, HttpSession session) {

        // 入库并返回已经登录
        HokageUserDO userDO = UserConverter.registerFormToDO(userForm);

        ServiceResponse<HokageUserDO> res = userService.register(userDO);

        if (res.getSucceeded()) {
            session.setAttribute(Constant.USER_SESSION_KEY, JSON.toJSONString(userDO));
            return success(UserConverter.DOToRegisterForm(res.getData()));
        }
        return fail(res.getCode(), res.getMsg());
    }

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public ResultVO<HokageUserRegisterForm> login(@RequestBody @Valid HokageUserLoginForm userForm, HttpSession session) {
        HokageUserDO userDO = UserConverter.loginFormToDO(userForm);
        ServiceResponse<HokageUserDO> res = userService.login(userDO);

        if (res.getSucceeded()) {
            session.setAttribute(Constant.USER_SESSION_KEY, JSON.toJSONString(userDO));
            return success(UserConverter.DOToRegisterForm(res.getData()));
        }
        return fail(res.getCode(), res.getMsg());
    }

    @RequestMapping(value = "/user/modify", method = RequestMethod.POST)
    public ResultVO<HokageUserRegisterForm> modify(@RequestBody @Valid HokageUserRegisterForm userForm) {
        return success(userForm);
    }
}
