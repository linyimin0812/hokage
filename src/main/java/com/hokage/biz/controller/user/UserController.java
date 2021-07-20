package com.hokage.biz.controller.user;

import com.alibaba.fastjson.JSON;
import com.hokage.biz.Constant;
import com.hokage.biz.converter.user.SearchConverter;
import com.hokage.biz.converter.user.UserConverter;
import com.hokage.biz.enums.ResultCodeEnum;
import com.hokage.biz.enums.UserRoleEnum;
import com.hokage.biz.form.user.*;
import com.hokage.biz.request.UserQuery;
import com.hokage.biz.response.user.HokageUserVO;
import com.hokage.biz.service.HokageUserService;
import com.hokage.common.BaseController;
import com.hokage.common.ResultVO;
import com.hokage.common.ServiceResponse;
import com.hokage.persistence.dataobject.HokageUserDO;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * @author linyimin
 * @date 2020/8/23 1:32pm
 * @email linyimin520812@gmail.com
 * @description user controller
 */
@RestController
public class UserController extends BaseController {

    private HokageUserService userService;

    private SearchConverter searchConverter;

    @Autowired
    public void setSearchConverter(SearchConverter converter) {
        this.searchConverter = converter;
    }

    @Autowired
    public void setUserService(HokageUserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    public ResultVO<HokageUserRegisterForm> register(@RequestBody @Valid HokageUserRegisterForm userForm, HttpSession session) {

        // persistent and return logged status
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

    @RequestMapping(value = "/user/logout", method = RequestMethod.POST)
    public ResultVO<Boolean> logout(@RequestBody @Valid HokageUserLogoutForm form, HttpSession session) {
        if (Objects.isNull(session.getAttribute(Constant.USER_SESSION_KEY))) {
            return fail(ResultCodeEnum.USER_NO_LOGIN.getCode(), ResultCodeEnum.USER_NO_LOGIN.getMsg());
        }
        session.removeAttribute(Constant.USER_SESSION_KEY);

        return success(true);
    }
}
