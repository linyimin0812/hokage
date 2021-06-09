package com.hokage.biz.controller;

import com.hokage.biz.enums.ResultCodeEnum;
import com.hokage.biz.form.file.FileOperateForm;
import com.hokage.biz.response.file.FileContentVO;
import com.hokage.biz.response.file.HokageFileVO;
import com.hokage.biz.service.HokageFileManagementService;
import com.hokage.common.BaseController;
import com.hokage.common.ResultVO;
import com.hokage.common.ServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
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
        return response(response);
    }

    @RequestMapping(value = "/server/file/open", method = RequestMethod.POST)
    public ResultVO<FileContentVO> openFile(@RequestBody FileOperateForm form) throws Exception {
        String serverKey = form.buildKey();
        ServiceResponse<FileContentVO> response = fileService.open(serverKey, form.getCurDir());

        return response(response);
    }

    @RequestMapping(value = "/server/file/rm", method = RequestMethod.POST)
    public ResultVO<Boolean> rmFile(@RequestBody FileOperateForm form) throws Exception {
        String serverKey = form.buildKey();
        ServiceResponse<Boolean> response = fileService.rm(serverKey, form.getCurDir());

        return response(response);
    }

    @RequestMapping(value = "/server/file/download", method = RequestMethod.GET)
    public void downloadFile(HttpServletResponse response,
                                          @RequestParam("id") Long id,
                                          @RequestParam("file") String file) throws Exception {
        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");

        String fileName = StringUtils.substring(file, StringUtils.lastIndexOf(file, '/') + 1);

        response.setHeader("Content-Disposition", "attachment;filename=" + fileName );

        OutputStream os  = response.getOutputStream();
        fileService.download(id, file, os);
    }

    @RequestMapping(value = "/server/file/tar", method = RequestMethod.POST)
    public ResultVO<Boolean> packageFile(@RequestBody FileOperateForm form) throws Exception {
        String serverKey = form.buildKey();
        ServiceResponse<Boolean> response = fileService.tar(serverKey, form.getCurDir());

        return response(response);
    }

    @RequestMapping(value = "/server/file/upload", method = RequestMethod.POST)
    public ResultVO<Boolean> uploadFile(@RequestParam(value = "file") MultipartFile file,@RequestParam("id") Long id,  @RequestParam("curDir") String curDir) throws IOException {

        if (file.isEmpty()) {
            return fail(ResultCodeEnum.SERVER_UPLOAD_FILE_ERROR.getCode(), ResultCodeEnum.SERVER_UPLOAD_FILE_ERROR.getMsg());
        }
        InputStream in = file.getInputStream();

        String fileName = ObjectUtils.defaultIfNull(file.getOriginalFilename(), "");
        String dst = Paths.get(curDir).resolve(fileName).toAbsolutePath().toString();

        ServiceResponse<Boolean> response = fileService.upload(id, dst, in);

        return response(response);

    }
}
