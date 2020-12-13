package com.banzhe.hokage.biz.controller;

import com.alibaba.fastjson.JSON;
import com.banzhe.hokage.biz.converter.UserConverter;
import com.banzhe.hokage.biz.enums.UserErrorCodeEnum;
import com.banzhe.hokage.biz.form.user.*;
import com.banzhe.hokage.biz.response.user.HokageUserVO;
import com.banzhe.hokage.biz.service.HokageUserService;
import com.banzhe.hokage.common.BaseController;
import com.banzhe.hokage.common.ResultVO;
import com.banzhe.hokage.common.ServiceResponse;
import com.banzhe.hokage.persistence.dataobject.HokageUserDO;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Collections;
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
            session.setAttribute(userDO.getEmail(), JSON.toJSONString(userDO));
            return success(UserConverter.DOToRegisterForm(res.getData()));
        }
        return fail(res.getCode(), res.getMsg());
    }

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public ResultVO<HokageUserRegisterForm> login(@RequestBody @Valid HokageUserLoginForm userForm, HttpSession session) {
        HokageUserDO userDO = UserConverter.loginFormToDO(userForm);
        ServiceResponse<HokageUserDO> res = userService.login(userDO);

        if (res.getSucceeded()) {
            session.setAttribute(userDO.getEmail(), JSON.toJSONString(userDO));
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
        if (Objects.isNull(session.getAttribute(form.getEmail()))) {
            return fail(UserErrorCodeEnum.USER_NO_LOGIN.getCode(), UserErrorCodeEnum.USER_NO_LOGIN.getMsg());
        }
        session.removeAttribute(form.getEmail());

        return success(true);
    }

    @RequestMapping(value = "/user/supervisor/list", method = RequestMethod.GET)
    public ResultVO<List<HokageUserVO>> listSupervisor() {
        ServiceResponse<List<HokageUserVO>> res = userService.listSupervisors();

        if (res.getSucceeded()) {
            return success(res.getData());
        }
        return fail(res.getCode(), res.getMsg());
    }

    @RequestMapping(value = "/user/supervisor/search", method = RequestMethod.POST)
    public ResultVO<List<HokageUserVO>> searchSupervisor(@RequestBody UserServerSearchForm form) {
        ServiceResponse<List<HokageUserVO>> response = userService.searchSupervisors(form);

        if (response.getSucceeded()) {
            return success(response.getData());
        }

        return fail(response.getCode(), response.getMsg());
    }

    @RequestMapping(value = "/user/supervisor/add", method = RequestMethod.POST)
    public ResultVO<Boolean> addSupervisors(@RequestBody UserServerOperateForm form) {

        ServiceResponse<Boolean> response = userService.addSupervisor(form.getUserIds());

        if (response.getSucceeded()) {
            return success(response.getData());
        }
        return fail(response.getCode(), response.getMsg());
    }


    @RequestMapping(value = "/user/supervisor/delete", method = RequestMethod.POST)
    public ResultVO<Boolean> delSupervisors(@RequestBody UserServerOperateForm form) {
        ServiceResponse<Boolean> response = userService.deleteSupervisor(form.getUserIds());

        if (response.getSucceeded()) {
            return success(response.getData());
        }
        return fail(response.getCode(), response.getMsg());
    }


    // TODO: 查看管理员信息
    @RequestMapping(value = "/user/supervisor/view", method = RequestMethod.POST)
    public ResultVO<HokageUserVO> viewSupervisors(@RequestParam Long id) {
        return success(new HokageUserVO());
    }

    @RequestMapping(value = "/user/supervisor/server/grant", method = RequestMethod.POST)
    public ResultVO<Boolean> grantSupervisorServer(@RequestBody UserServerOperateForm form) {
        List<Long> supervisorIds = form.getUserIds();
        Preconditions.checkNotNull(supervisorIds);

        ServiceResponse<Boolean> response = userService.grantSupervisor(supervisorIds.get(0), form.getServerIds());

        if (response.getSucceeded()) {
            return success(response.getData());
        }
        return fail(response.getCode(), response.getMsg());
    }

    @RequestMapping(value = "/user/supervisor/server/recycle", method = RequestMethod.POST)
    public ResultVO<Boolean> recycleSupervisorServer(@RequestBody UserServerOperateForm form) {
        ServiceResponse<Boolean> response = null;
        List<Long> supervisorIds = form.getServerIds();
        Preconditions.checkNotNull(supervisorIds);
        if (Objects.nonNull(form.getServerIds())) {
            response = userService.recycleSupervisor(supervisorIds.get(0), form.getServerIds());
        } else {
            response = userService.recycleSupervisor(form.getUserIds().get(0));
        }

        if (response.getSucceeded()) {
            return success(response.getData());
        }
        return fail(response.getCode(), response.getMsg());
    }

    @RequestMapping(value = "/user/subordinate/list", method = RequestMethod.GET)
    public ResultVO<List<HokageUserVO>> listSubordinate(@RequestParam UserServerSearchForm form) {

        Preconditions.checkNotNull(form.getId(), "operation id can't be null");

        ServiceResponse<List<HokageUserVO>> res = userService.listOrdinaryUsers(form.getId());

        if (res.getSucceeded()) {
            return success(res.getData());
        }
        return fail(res.getCode(), res.getMsg());
    }

    @RequestMapping(value = "/user/subordinate/search", method = RequestMethod.POST)
    public ResultVO<List<HokageUserVO>> searchSubordinate(@RequestBody UserServerSearchForm form) {
        return success(Collections.emptyList());
    }

    @RequestMapping(value = "/user/subordinate/add", method = RequestMethod.POST)
    public ResultVO<Boolean> addSubordinate(@RequestBody UserServerOperateForm form) {

        Preconditions.checkNotNull(form.getId(), "operationId can't be null");
        Preconditions.checkArgument(!CollectionUtils.isEmpty(form.getUserIds()), "user ids can't be empty");

        ServiceResponse<Boolean> response = userService.addSubordinate(form.getId(), form.getUserIds());

        if (response.getSucceeded()) {
            return success(response.getData());
        }
        return fail(response.getCode(), response.getMsg());

    }

    @RequestMapping(value = "/user/subordinate/delete", method = RequestMethod.POST)
    public ResultVO<Boolean> delSubordinate(@RequestBody UserServerOperateForm form) {

        Preconditions.checkNotNull(form.getId(), "operationId can't be null");
        Preconditions.checkArgument(!CollectionUtils.isEmpty(form.getUserIds()), "user ids can't be empty");

        ServiceResponse<Boolean> response = userService.deleteSubordinate(form.getId(), form.getUserIds());

        if (response.getSucceeded()) {
            return success(response.getData());
        }
        return fail(response.getCode(), response.getMsg());
    }


    // TODO: 查看用户信息
    @RequestMapping(value = "/user/subordinate/view", method = RequestMethod.POST)
    public ResultVO<HokageUserVO> viewSubordinate(@RequestParam Long id) {
        return success(new HokageUserVO());
    }

    @RequestMapping(value = "/user/subordinate/server/grant", method = RequestMethod.POST)
    public ResultVO<Boolean> grantSubordinateServer(@RequestBody UserServerOperateForm form) {

        Preconditions.checkArgument(!CollectionUtils.isEmpty(form.getUserIds()));
        Preconditions.checkArgument(!CollectionUtils.isEmpty(form.getServerIds()));

        ServiceResponse<Boolean> response = userService.grantSupervisor(form.getUserIds().get(0), form.getServerIds());

        if (response.getSucceeded()) {
            return success(response.getData());
        }
        return fail(response.getCode(), response.getMsg());
    }

    // TODO: 回收普通用户服务器权限
    @RequestMapping(value = "/user/subordinate/server/recycle", method = RequestMethod.POST)
    public ResultVO<Boolean> recycleSubordinateServer(@RequestBody UserServerOperateForm form) {
        return success(Boolean.TRUE);
    }


}
