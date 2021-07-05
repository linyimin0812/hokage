package com.hokage.biz.controller.user;

import com.google.common.base.Preconditions;
import com.hokage.biz.converter.user.SearchConverter;
import com.hokage.biz.enums.UserRoleEnum;
import com.hokage.biz.form.user.UserServerOperateForm;
import com.hokage.biz.form.user.UserServerSearchForm;
import com.hokage.biz.request.UserQuery;
import com.hokage.biz.response.user.HokageUserVO;
import com.hokage.biz.service.HokageUserService;
import com.hokage.common.BaseController;
import com.hokage.common.ResultVO;
import com.hokage.common.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
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

    private SearchConverter searchConverter;

    @Autowired
    public void setSearchConverter(SearchConverter converter) {
        this.searchConverter = converter;
    }

    @Autowired
    public void setUserService(HokageUserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/user/supervisor/search", method = RequestMethod.POST)
    public ResultVO<List<HokageUserVO>> searchSupervisor(@RequestBody UserServerSearchForm form) {

        UserQuery query = searchConverter.doForward(form);
        query.setRole(UserRoleEnum.supervisor.getValue());

        ServiceResponse<List<HokageUserVO>> response = userService.search(query);
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
        ServiceResponse<Boolean> response;
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
}
