package com.hokage.biz.controller;

import com.hokage.biz.form.file.FileOperateForm;
import com.hokage.biz.response.file.FileContentVO;
import com.hokage.biz.response.file.HokageFileVO;
import com.hokage.biz.service.HokageFileManagementService;
import com.hokage.common.BaseController;
import com.hokage.common.ResultVO;
import com.hokage.common.ServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * @author linyimin
 * @date 2020/8/23 1:51
 * @email linyimin520812@gmail.com
 * @description
 */
@Slf4j
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

    @RequestMapping(value = "/server/file/download", method = RequestMethod.GET)
    public void downloadFile(HttpServletResponse response,
                                          @RequestParam("serverKey") String serverKey,
                                          @RequestParam("file") String file) throws Exception {
        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + file );

        OutputStream os  = response.getOutputStream();
        fileService.download(serverKey, file, os);
    }
}
