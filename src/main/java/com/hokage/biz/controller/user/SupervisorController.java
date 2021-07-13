package com.hokage.biz.controller.user;

import com.google.common.base.Preconditions;
import com.hokage.biz.converter.server.SupervisorServerConverter;
import com.hokage.biz.converter.user.SupervisorConverter;
import com.hokage.biz.form.user.UserServerOperateForm;
import com.hokage.biz.form.user.UserSearchForm;
import com.hokage.biz.request.server.SupervisorServerQuery;
import com.hokage.biz.request.user.SupervisorQuery;
import com.hokage.biz.response.server.HokageServerVO;
import com.hokage.biz.response.user.HokageUserVO;
import com.hokage.biz.service.HokageUserService;
import com.hokage.common.BaseController;
import com.hokage.common.ResultVO;
import com.hokage.common.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * @author yiminlin
 * @date 2021/07/06 12:58 am
 * @description supervisor controller
 **/
@RestController
public class SupervisorController extends BaseController {

    private HokageUserService userService;
    private SupervisorConverter supervisorConverter;
    private SupervisorServerConverter supServerConverter;

    @Autowired
    public void setSupervisorConverter(SupervisorConverter supervisorConverter) {
        this.supervisorConverter = supervisorConverter;
    }

    @Autowired
    public void setUserService(HokageUserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setSupServerConverter(SupervisorServerConverter supServerConverter) {
        this.supServerConverter = supServerConverter;
    }

    @RequestMapping(value = "/user/supervisor/search", method = RequestMethod.POST)
    public ResultVO<List<HokageUserVO>> searchSupervisor(@RequestBody UserSearchForm form) {
        SupervisorQuery query = supervisorConverter.doForward(form);
        ServiceResponse<List<HokageUserVO>> response = userService.searchSupervisor(query);
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
        // TODO: 切面判断是否有权限
        ServiceResponse<Boolean> response = userService.deleteSupervisor(form.getUserIds().get(0));

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

    // TODO: 切面判断权限
    @RequestMapping(value = "/user/supervisor/server/recycle", method = RequestMethod.POST)
    public ResultVO<Boolean> recycleSupervisorServer(@RequestBody UserServerOperateForm form) {

        Preconditions.checkState(!CollectionUtils.isEmpty(form.getUserIds()), "supervisor id can;t be empty");
        Preconditions.checkState(!CollectionUtils.isEmpty(form.getServerIds()), "server id can;t be empty");

        List<Long> supervisorIds = form.getUserIds();

        ServiceResponse<Boolean> response = userService.recycleSupervisor(supervisorIds.get(0), form.getServerIds());

        if (response.getSucceeded()) {
            return success(response.getData());
        }
        return fail(response.getCode(), response.getMsg());
    }

    @RequestMapping(value = "/user/supervisor/server/search", method = RequestMethod.POST)
    public ResultVO<List<HokageServerVO>> searchSupervisorServer(@RequestBody UserServerOperateForm form) {

        Preconditions.checkState(!CollectionUtils.isEmpty(form.getUserIds()), "supervisor id can't be empty");

        SupervisorServerQuery query = supServerConverter.doForward(form);

        ServiceResponse<List<HokageServerVO>> response = userService.searchSupervisorServer(query);

        if (response.getSucceeded()) {
            return success(response.getData());
        }
        return fail(response.getCode(), response.getMsg());
    }
}
