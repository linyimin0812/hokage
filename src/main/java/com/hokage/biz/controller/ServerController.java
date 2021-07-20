package com.hokage.biz.controller;

import com.google.common.base.Preconditions;
import com.hokage.biz.converter.server.ServerFormConverter;
import com.hokage.biz.converter.server.ServerSearchConverter;
import com.hokage.biz.enums.ResultCodeEnum;
import com.hokage.biz.form.server.HokageServerForm;
import com.hokage.biz.form.server.ServerOperateForm;
import com.hokage.biz.form.server.ServerSearchForm;
import com.hokage.biz.request.server.AllServerQuery;
import com.hokage.biz.request.server.ServerQuery;
import com.hokage.biz.request.server.SubordinateServerQuery;
import com.hokage.biz.request.server.SupervisorServerQuery;
import com.hokage.biz.response.server.HokageServerVO;
import com.hokage.biz.response.server.ServerAccountVO;
import com.hokage.biz.service.HokageServerService;
import com.hokage.common.BaseController;
import com.hokage.common.ResultVO;
import com.hokage.common.ServiceResponse;
import com.hokage.persistence.dao.HokageServerSshKeyContentDao;
import com.hokage.persistence.dataobject.HokageServerDO;
import com.hokage.persistence.dataobject.HokageServerSshKeyContentDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

/**
 * @author linyimin
 * @date 2020/8/23 1:50 am
 * @email linyimin520812@gmail.com
 * @description server controller
 */
@RestController
public class ServerController extends BaseController {

    private HokageServerService serverService;

    private HokageServerSshKeyContentDao contentDao;

    private ServerFormConverter serverFormConverter;

    @Autowired
    public void setServerService(HokageServerService serverService) {
        this.serverService = serverService;
    }

    @Autowired
    public void setContentDao(HokageServerSshKeyContentDao contentDao) {
        this.contentDao = contentDao;
    }

    @Autowired
    public void setServerFormConverter(ServerFormConverter serverFormConverter) {
        this.serverFormConverter = serverFormConverter;
    }

    @RequestMapping(value = "/server/search", method = RequestMethod.POST)
    public ResultVO<List<HokageServerVO>> searchServer(@RequestBody ServerSearchForm form) {
        ServerQuery query = ServerSearchConverter.converter(form);
        ServiceResponse<List<HokageServerVO>> response = serverService.searchServer(query);
        return response(response);
    }

    @RequestMapping(value = "/server/view", method = RequestMethod.GET)
    public ResultVO<HokageServerVO> viewServer(@RequestParam("id") Long id) {
        ServiceResponse<HokageServerVO> response = serverService.viewServer(id);
        return response(response);
    }

    @RequestMapping(value = "/server/account/list", method = RequestMethod.GET)
    public ResultVO<List<ServerAccountVO>> serverAccountList(@RequestParam("id") Long id) {
        ServiceResponse<List<ServerAccountVO>> response = serverService.listServerAccount(id);
        return response(response);
    }

    @RequestMapping(value = "/server/all/search", method = RequestMethod.POST)
    public ResultVO<List<HokageServerVO>> searchAllServer(@RequestBody ServerSearchForm form) {
        AllServerQuery allServerQuery = ServerSearchConverter.converterToAll(form);
        ServiceResponse<List<HokageServerVO>> response = serverService.searchAllServer(allServerQuery);
        return response(response);
    }

    @RequestMapping(value = "/server/supervisor/search", method = RequestMethod.POST)
    public ResultVO<List<HokageServerVO>> searchSupervisorServer(@RequestBody ServerSearchForm form) {
        SupervisorServerQuery query = ServerSearchConverter.converterToSupervisor(form);
        ServiceResponse<List<HokageServerVO>> response = serverService.searchSupervisorServer(query);
        return response(response);
    }

    @RequestMapping(value = "/server/subordinate/search", method = RequestMethod.POST)
    public ResultVO<List<HokageServerVO>> searchSubordinateServer(@RequestBody ServerSearchForm form) {
        SubordinateServerQuery query = ServerSearchConverter.converterToSubordinate(form);
        ServiceResponse<List<HokageServerVO>> response = serverService.searchSubordinateServer(query);
        return response(response);
    }

    @RequestMapping(value = "/server/save", method = RequestMethod.POST)
    public ResultVO<HokageServerForm> addServer(@RequestBody HokageServerForm form) {

        HokageServerDO serverDO = serverFormConverter.doForward(form);
        ServiceResponse<HokageServerDO> response = serverService.save(serverDO);

        if (!response.getSucceeded()) {
            return fail(response.getCode(), response.getMsg());
        }

        form = serverFormConverter.doBackward(serverDO);

        return success(form);
    }

    @RequestMapping(value = "/server/delete", method = RequestMethod.POST)
    public ResultVO<Boolean> delServer(@RequestBody ServerOperateForm form) {
        // TODO: 切面判断权限
        Preconditions.checkState(!CollectionUtils.isEmpty(form.getServerIds()), "server id list can't be empty");
        Long serverId = form.getServerIds().get(0);
        ServiceResponse<Boolean> response = serverService.delete(serverId);

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
        return response(response);
    }


    @RequestMapping(value = "/app/file/upload", method = RequestMethod.POST)
    public ResultVO<String> uploadFile(@RequestParam(value = "sshKeyFile") MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            return fail(ResultCodeEnum.SERVER_UPLOAD_FILE_ERROR.getCode(), ResultCodeEnum.SERVER_UPLOAD_FILE_ERROR.getMsg());
        }

        String uid = UUID.randomUUID().toString();

        String content = new String(file.getBytes(), StandardCharsets.UTF_8);;
        HokageServerSshKeyContentDO contentDO = new HokageServerSshKeyContentDO();
        contentDO.setUid(uid);
        contentDO.setContent(content);

        if (contentDao.insert(contentDO) > 0) {
            return success(uid);
        }

        return fail(ResultCodeEnum.SERVER_SYSTEM_ERROR.getCode(), ResultCodeEnum.SERVER_SYSTEM_ERROR.getMsg());

    }

    @RequestMapping(value = "/supervisor/grant/server/list", method = RequestMethod.GET)
    public ResultVO<List<HokageServerVO>> listSupervisorGrantServer(@RequestParam("id") Long supervisorId) {
        ServiceResponse<List<HokageServerVO>> response = serverService.listSupervisorGrantServer(supervisorId);
        return response(response);
    }

    @RequestMapping(value = "/supervisor/not/grant/server/list", method = RequestMethod.GET)
    public ResultVO<List<HokageServerVO>> listNotGrantServer(@RequestParam("id") Long supervisorId) {
        ServiceResponse<List<HokageServerVO>> response = serverService.listNotGrantServer(supervisorId);
        return response(response);
    }
}
