package com.hokage.biz.controller.user;

import com.google.common.base.Preconditions;
import com.hokage.biz.converter.user.SubordinateConverter;
import com.hokage.biz.converter.user.SupervisorConverter;
import com.hokage.biz.enums.UserRoleEnum;
import com.hokage.biz.form.user.UserOperateForm;
import com.hokage.biz.form.user.UserSearchForm;
import com.hokage.biz.form.user.UserServerOperateForm;
import com.hokage.biz.request.UserQuery;
import com.hokage.biz.request.user.SubordinateQuery;
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
 * @date 2021/07/12 8:55 pm
 * @description subordinate controller
 **/
@RestController
public class SubordinateController extends BaseController {

    private HokageUserService userService;
    private SubordinateConverter subordinateConverter;

    @Autowired
    public void setUserService(HokageUserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setSubordinateConverter(SubordinateConverter converter) {
        this.subordinateConverter = converter;
    }

    @RequestMapping(value = "/user/subordinate/all", method = RequestMethod.GET)
    public ResultVO<List<HokageUserVO>> listAllSubordinate() {

        ServiceResponse<List<HokageUserVO>> res = userService.listAllOrdinateUsers();

        if (res.getSucceeded()) {
            return success(res.getData());
        }
        return fail(res.getCode(), res.getMsg());
    }

    @RequestMapping(value = "/user/subordinate/search", method = RequestMethod.POST)
    public ResultVO<List<HokageUserVO>> searchSubordinate(@RequestBody UserSearchForm form) {

        SubordinateQuery query = subordinateConverter.doForward(form);
        ServiceResponse<List<HokageUserVO>> response = userService.searchSubordinate(query);
        if (response.getSucceeded()) {
            return success(response.getData());
        }
        return fail(response.getCode(), response.getMsg());
    }

    @RequestMapping(value = "/user/subordinate/supervisor/grant", method = RequestMethod.POST)
    public ResultVO<Boolean> addSubordinates2Supervisor(@RequestBody UserOperateForm form) {

        Preconditions.checkNotNull(form.getOperatorId(), "operationId can't be null");
        Preconditions.checkArgument(!CollectionUtils.isEmpty(form.getUserIds()), "user ids can't be empty");

        ServiceResponse<Boolean> response = userService.addSubordinates2Supervisor(form.getSupervisorId(), form.getUserIds());

        if (response.getSucceeded()) {
            return success(response.getData());
        }
        return fail(response.getCode(), response.getMsg());

    }

    @RequestMapping(value = "/user/subordinate/supervisor/recycle", method = RequestMethod.POST)
    public ResultVO<Boolean> deleteSupervisor(@RequestBody UserOperateForm form) {

        Preconditions.checkNotNull(form.getOperatorId(), "operationId can't be null");
        Preconditions.checkArgument(!CollectionUtils.isEmpty(form.getUserIds()), "user ids can't be empty");

        ServiceResponse<Boolean> response = userService.recycleSubordinates2Supervisor(form.getSupervisorId(), form.getUserIds());

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

        ServiceResponse<Boolean> response = userService.grantSubordinate(form.getUserIds().get(0), form.getServerIds());

        if (response.getSucceeded()) {
            return success(response.getData());
        }
        return fail(response.getCode(), response.getMsg());
    }

    @RequestMapping(value = "/user/subordinate/server/recycle", method = RequestMethod.POST)
    public ResultVO<Boolean> recycleSubordinateServer(@RequestBody UserServerOperateForm form) {

        Preconditions.checkArgument(!CollectionUtils.isEmpty(form.getUserIds()));
        Preconditions.checkArgument(!CollectionUtils.isEmpty(form.getServerIds()));

        ServiceResponse<Boolean> response;

        if (Objects.nonNull(form.getServerIds())) {
            response = userService.recycleSubordinate(form.getUserIds().get(0), form.getServerIds());
        } else {
            response = userService.recycleSubordinate(form.getUserIds().get(0));
        }

        if (response.getSucceeded()) {
            return success(response.getData());
        }
        return fail(response.getCode(), response.getMsg());
    }
}
