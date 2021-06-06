package com.hokage.biz.controller;

import com.hokage.biz.form.file.FileOperateForm;
import com.hokage.biz.response.file.FileContentVO;
import com.hokage.biz.response.file.HokageFileVO;
import com.hokage.biz.service.HokageFileManagementService;
import com.hokage.common.BaseController;
import com.hokage.common.ResultVO;
import com.hokage.common.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author linyimin
 * @date 2020/8/23 1:51
 * @email linyimin520812@gmail.com
 * @description
 */
@RestController
public class FileManagerController extends BaseController {

    private HokageFileManagementService fileService;

    @Autowired
    public void setFileManagementService(HokageFileManagementService fileManagementService) {
        this.fileService = fileManagementService;
    }

    @RequestMapping(value = "/server/file/list", method = RequestMethod.POST)
    public ResultVO<HokageFileVO> listFile(@RequestBody FileOperateForm form) throws Exception {
        String serverKey = form.buildKey();
        ServiceResponse<HokageFileVO> response = fileService.list(serverKey, form.getCurDir(), new ArrayList<>());

        if (response.getSucceeded()) {
            return success(response.getData());
        }

        return fail(response.getCode(), response.getMsg());
    }

    @RequestMapping(value = "/server/file/open", method = RequestMethod.POST)
    public ResultVO<FileContentVO> openFile(@RequestBody FileOperateForm form) throws Exception {
        String serverKey = form.buildKey();
        ServiceResponse<FileContentVO> response = fileService.open(serverKey, form.getCurDir());

        if (response.getSucceeded()) {
            return success(response.getData());
        }

        return fail(response.getCode(), response.getMsg());
    }

    @RequestMapping(value = "/server/file/rm", method = RequestMethod.POST)
    public ResultVO<Boolean> rmFile(@RequestBody FileOperateForm form) throws Exception {
        String serverKey = form.buildKey();
        ServiceResponse<Boolean> response = fileService.rm(serverKey, form.getCurDir());

        if (response.getSucceeded()) {
            return success(response.getData());
        }

        return fail(response.getCode(), response.getMsg());
    }
}
