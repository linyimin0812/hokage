package com.hokage.biz.controller;

import com.google.common.base.Preconditions;
import com.hokage.biz.converter.server.ServerGroupConverter;
import com.hokage.biz.form.server.ServerOperateForm;
import com.hokage.biz.response.HokageOptionVO;
import com.hokage.biz.service.HokageServerGroupService;
import com.hokage.common.BaseController;
import com.hokage.common.ResultVO;
import com.hokage.common.ServiceResponse;
import com.hokage.persistence.dataobject.HokageServerGroupDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author linyimin
 * @date 2021/2/27 20:43
 * @email linyimin520812@gmail.com
 * @description server group controller
 */
@RestController
public class ServerGroupController extends BaseController {

    private HokageServerGroupService serverGroupService;
    private ServerGroupConverter serverGroupConverter;

    @Autowired
    public void setServerGroupService(HokageServerGroupService serverGroupService) {
        this.serverGroupService = serverGroupService;
    }

    @Autowired
    public void setServerGroupConverter(ServerGroupConverter serverGroupConverter) {
        this.serverGroupConverter = serverGroupConverter;
    }

    @RequestMapping(value = "/server/group/list", method = RequestMethod.GET)
    public ResultVO<List<HokageOptionVO<String>>> listServerGroup(@RequestParam Long id) {
        ServiceResponse<List<HokageOptionVO<String>>> optionListResult = serverGroupService.listGroupOptions();
        if (optionListResult.getSucceeded()) {
            return success(optionListResult.getData());
        }

        return fail(optionListResult.getCode(), optionListResult.getMsg());
    }

    @RequestMapping(value = "/server/group/add", method = RequestMethod.POST)
    public ResultVO<List<HokageOptionVO<String>>> addServerGroup(@RequestBody ServerOperateForm form) {
        Preconditions.checkNotNull(form.getOperatorId(), "operator id can't be null");
        form.getServerGroup().setCreatorId(form.getOperatorId());
        HokageServerGroupDO groupDO = serverGroupConverter.doForward(form.getServerGroup());
        ServiceResponse<Boolean> saveResult = serverGroupService.addGroup(groupDO);
        if (saveResult.getSucceeded()) {
            ServiceResponse<List<HokageOptionVO<String>>> optionListResult = serverGroupService.listGroupOptions();
            if (optionListResult.getSucceeded()) {
                return success(optionListResult.getData());
            }
            return fail(optionListResult.getCode(), optionListResult.getMsg());
        }

        return fail(saveResult.getCode(), saveResult.getMsg());
    }
}
