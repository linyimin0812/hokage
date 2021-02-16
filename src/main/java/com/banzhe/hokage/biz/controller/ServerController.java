package com.banzhe.hokage.biz.controller;

import com.banzhe.hokage.biz.form.server.HokageServerForm;
import com.banzhe.hokage.biz.form.server.ServerOperateForm;
import com.banzhe.hokage.biz.form.server.ServerSearchForm;
import com.banzhe.hokage.biz.response.server.HokageServerVO;
import com.banzhe.hokage.biz.service.HokageServerService;
import com.banzhe.hokage.common.BaseController;
import com.banzhe.hokage.common.ResultVO;
import com.banzhe.hokage.common.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * @author linyimin
 * @date 2020/8/23 1:50 am
 * @email linyimin520812@gmail.com
 * @description server controller
 */
@RestController
public class ServerController extends BaseController {

    private HokageServerService serverService;

    @Autowired
    public void setServerService(HokageServerService serverService) {
        this.serverService = serverService;
    }

    /**
     * list all server
     */
    @RequestMapping(value = "/server/list", method = RequestMethod.GET)
    public ResultVO<List<HokageServerVO>> listServer() {
        ServiceResponse<List<HokageServerVO>> serviceResponse = serverService.selectAll();
        if (serviceResponse.getSucceeded()) {
            return success(Collections.emptyList());
        }
        return fail(serviceResponse.getCode(), serviceResponse.getMsg());
    }

    @RequestMapping(value = "/server/search", method = RequestMethod.POST)
    public ResultVO<List<HokageServerVO>> searchServer(@RequestBody ServerSearchForm form) {

        ServiceResponse<List<HokageServerVO>> response = serverService.listServer(form);

        if (!response.getSucceeded()) {
            return fail(response.getCode(), response.getMsg());
        }

        return success(response.getData());
    }

    @RequestMapping(value = "/server/add", method = RequestMethod.POST)
    public ResultVO<HokageServerForm> addServer(@RequestBody HokageServerForm form) {

        ServiceResponse<HokageServerForm> response = serverService.save(form);

        if (!response.getSucceeded()) {
            return fail(response.getCode(), response.getMsg());
        }

        return success(response.getData());
    }

    @RequestMapping(value = "/server/delete", method = RequestMethod.POST)
    public ResultVO<Boolean> delServer(@RequestBody ServerOperateForm form) {
        ServiceResponse<Boolean> response = serverService.delete(form);

        if (!response.getSucceeded()) {
            return fail(response.getCode(), response.getMsg());
        }

        return success(Boolean.TRUE);
    }

    @RequestMapping(value = "/server/supervisor/add", method = RequestMethod.POST)
    public ResultVO<Boolean> addServerSupervisor(@RequestBody ServerOperateForm form) {
        ServiceResponse<Boolean> response = serverService.designateSupervisor(form);

        if (!response.getSucceeded()) {
            return fail(response.getCode(), response.getMsg());
        }

        return success(Boolean.TRUE);
    }

    @RequestMapping(value = "/server/supervisor/delete", method = RequestMethod.POST)
    public ResultVO<Boolean> delServerSupervisor(@RequestBody ServerOperateForm form) {
        ServiceResponse<Boolean> response = serverService.revokeSupervisor(form);

        if (!response.getSucceeded()) {
            return fail(response.getCode(), response.getMsg());
        }

        return success(Boolean.TRUE);
    }

    // TODO: 列举服务器信息: 超级管理员、管理员和普通用户使用统一接口？
    @RequestMapping(value = "/supervisor/server/list", method = RequestMethod.GET)
    public ResultVO<List<HokageServerVO>> listSupervisorServer(@RequestParam Long id) {
        return success(Collections.emptyList());
    }

    @RequestMapping(value = "/server/apply", method = RequestMethod.POST)
    public ResultVO<Boolean> applyServer(@RequestBody ServerOperateForm form) {
        ServiceResponse<Boolean> response = serverService.applyServer(form);

        if (!response.getSucceeded()) {
            return fail(response.getCode(), response.getMsg());
        }

        return success(Boolean.TRUE);
    }

    @RequestMapping(value = "/server/subordinate/add", method = RequestMethod.POST)
    public ResultVO<Boolean> addServerSubordinate(@RequestBody ServerOperateForm form) {

        ServiceResponse<Boolean> response = serverService.designateSubordinate(form);

        if (!response.getSucceeded()) {
            return fail(response.getCode(), response.getMsg());
        }

        return success(Boolean.TRUE);
    }

    @RequestMapping(value = "/server/subordinate/delete", method = RequestMethod.POST)
    public ResultVO<Boolean> delServerSubordinate(@RequestBody ServerOperateForm form) {
        ServiceResponse<Boolean> response = serverService.revokeSubordinate(form);

        if (!response.getSucceeded()) {
            return fail(response.getCode(), response.getMsg());
        }

        return success(Boolean.TRUE);
    }
}
