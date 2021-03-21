package com.hokage.biz.controller;

import com.hokage.biz.enums.ResultCodeEnum;
import com.hokage.biz.form.server.HokageServerForm;
import com.hokage.biz.form.server.ServerOperateForm;
import com.hokage.biz.form.server.ServerSearchForm;
import com.hokage.biz.response.HokageOptionVO;
import com.hokage.biz.response.server.HokageServerVO;
import com.hokage.biz.service.HokageServerService;
import com.hokage.common.BaseController;
import com.hokage.common.ResultVO;
import com.hokage.common.ServiceResponse;
import com.hokage.persistence.dao.HokageServerSshKeyContentDao;
import com.hokage.persistence.dataobject.HokageServerSshKeyContentDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
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

    @Autowired
    public void setServerService(HokageServerService serverService) {
        this.serverService = serverService;
    }

    @Autowired
    public void setContentDao(HokageServerSshKeyContentDao contentDao) {
        this.contentDao = contentDao;
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

    @RequestMapping(value = "/server/label/list", method = RequestMethod.GET)
    public ResultVO<List<HokageOptionVO<String>>> listServerLabel() {
        List<HokageOptionVO<String>> optionVOList = Arrays.asList(
                new HokageOptionVO<>("请选择", ""),
                new HokageOptionVO<>("普通服务器", "X86"),
                new HokageOptionVO<>("GPU服务器", "GPU"),
                new HokageOptionVO<>("内网服务器", "internal"),
                new HokageOptionVO<>("外网服务器", "external")
        );

        return success(optionVOList);
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
}
