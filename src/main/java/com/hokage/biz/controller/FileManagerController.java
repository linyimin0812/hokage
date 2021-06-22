package com.hokage.biz.controller;

import com.hokage.biz.enums.ResultCodeEnum;
import com.hokage.biz.form.file.FileOperateForm;
import com.hokage.biz.request.command.FileParam;
import com.hokage.biz.response.file.FileContentVO;
import com.hokage.biz.response.file.HokageFileVO;
import com.hokage.biz.service.impl.HokageFileManagementService;
import com.hokage.common.BaseController;
import com.hokage.common.ResultVO;
import com.hokage.common.ServiceResponse;
import com.hokage.ssh.command.handler.FileCommandHandler;
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
import java.net.URLConnection;
import java.nio.file.Paths;

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
    private FileCommandHandler<FileParam> commandHandler;

    @Autowired
    public void setFileManagementService(HokageFileManagementService fileService) {
        this.fileService = fileService;
    }

    @Autowired
    public void setCommandHandler(FileCommandHandler<FileParam> commandHandler) {
        this.commandHandler = commandHandler;
    }

    @RequestMapping(value = "/server/file/list", method = RequestMethod.POST)
    public ResultVO<HokageFileVO> listFile(@RequestBody FileOperateForm form) throws Exception {
        String serverKey = form.buildKey();
        FileParam param = new FileParam();
        param.setPath(form.getPath());
        ServiceResponse<HokageFileVO> response = fileService.execute(serverKey, param, commandHandler.listHandler);
        return response(response);
    }

    @RequestMapping(value = "/server/file/open", method = RequestMethod.POST)
    public ResultVO<FileContentVO> openFile(@RequestBody FileOperateForm form) throws Exception {
        String serverKey = form.buildKey();
        Long page = ObjectUtils.defaultIfNull(form.getPage(), 1L);
        FileParam param = new FileParam();
        param.setPath(form.getPath()).setPage(page);
        ServiceResponse<FileContentVO> response = fileService.execute(serverKey, param, commandHandler.openHandler);

        return response(response);
    }

    @RequestMapping(value = "/server/file/rm", method = RequestMethod.POST)
    public ResultVO<Boolean> rmFile(@RequestBody FileOperateForm form) throws Exception {
        String serverKey = form.buildKey();
        FileParam param = new FileParam();
        param.setPath(form.getPath());
        ServiceResponse<Boolean> response = fileService.execute(serverKey, param, commandHandler.rmHandler);
        return response(response);
    }

    @RequestMapping(value = "/server/file/download", method = RequestMethod.GET)
    public void downloadFile(HttpServletResponse response,
                                          @RequestParam("serverKey") String serverKey,
                                          @RequestParam("file") String file) throws Exception {
        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");

        String fileName = StringUtils.substring(file, StringUtils.lastIndexOf(file, '/') + 1);

        response.setHeader("Content-Disposition", "attachment;filename=" + fileName );

        OutputStream os  = response.getOutputStream();
        FileParam param = new FileParam();
        param.setPath(file).setOs(os);
        fileService.ExecuteSftpCommand(serverKey, param, commandHandler.downloadHandler);
    }

    @RequestMapping(value = "/server/file/tar", method = RequestMethod.POST)
    public ResultVO<Boolean> packageFile(@RequestBody FileOperateForm form) throws Exception {
        String serverKey = form.buildKey();

        FileParam param = new FileParam();
        param.setPath(form.getPath());
        ServiceResponse<Boolean> response = fileService.execute(serverKey, param, commandHandler.tarHandler);

        return response(response);
    }

    @RequestMapping(value = "/server/file/upload", method = RequestMethod.POST)
    public ResultVO<Boolean> uploadFile(@RequestParam(value = "file") MultipartFile file,
                                        @RequestParam("serverKey") String serverKey,
                                        @RequestParam("curDir") String curDir) throws IOException {

        if (file.isEmpty()) {
            return fail(ResultCodeEnum.SERVER_UPLOAD_FILE_ERROR.getCode(), ResultCodeEnum.SERVER_UPLOAD_FILE_ERROR.getMsg());
        }
        InputStream in = file.getInputStream();

        String fileName = ObjectUtils.defaultIfNull(file.getOriginalFilename(), "");
        String dst = Paths.get(curDir).resolve(fileName).toAbsolutePath().toString();

        FileParam param = new FileParam();
        param.setDstPath(dst).setSrc(in);
        ServiceResponse<Boolean> response = fileService.ExecuteSftpCommand(serverKey, param, commandHandler.uploadHandler);

        return response(response);
    }

    @RequestMapping(value = "/server/file/move", method = RequestMethod.POST)
    public ResultVO<Boolean> moveFile(@RequestBody FileOperateForm form) throws Exception {
        String serverKey = form.buildKey();
        FileParam param = new FileParam();
        param.setSrcPath(form.getPath()).setDstPath(form.getDst());
        ServiceResponse<Boolean> response = fileService.execute(serverKey, param, commandHandler.moveHandler);

        return response(response);
    }

    @RequestMapping(value = "/server/file/chmod", method = RequestMethod.POST)
    public ResultVO<Boolean> chmodFile(@RequestBody FileOperateForm form) throws Exception {
        String serverKey = form.buildKey();
        FileParam param = new FileParam();
        param.setPath(form.getPath()).setPermission(form.getPermission());
        ServiceResponse<Boolean> response = fileService.execute(serverKey, param, commandHandler.chmodHandler);
        return response(response);
    }

    @RequestMapping(value = "/server/file/view", method = RequestMethod.GET)
    public void viewFile(HttpServletResponse response,
                             @RequestParam("serverKey") String serverKey,
                             @RequestParam("file") String file) throws Exception {
        response.reset();
        response.setContentType(URLConnection.guessContentTypeFromName(file));
        OutputStream os  = response.getOutputStream();
        FileParam param = new FileParam();
        param.setPath(file).setOs(os);
        fileService.ExecuteSftpCommand(serverKey, param, commandHandler.downloadHandler);
    }
}
