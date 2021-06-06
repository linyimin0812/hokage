package com.hokage.biz.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hokage.biz.converter.file.SshProperty2WebProperty;
import com.hokage.biz.enums.ResultCodeEnum;
import com.hokage.biz.response.file.FileContentVO;
import com.hokage.biz.response.file.HokageFileProperty;
import com.hokage.biz.response.file.HokageFileVO;
import com.hokage.biz.service.HokageFileManagementService;
import com.hokage.cache.HokageServerCacheDao;
import com.hokage.common.ServiceResponse;
import com.hokage.ssh.SshClient;
import com.hokage.ssh.command.AbstractCommand;
import com.hokage.ssh.command.CommandDispatcher;
import com.hokage.ssh.command.CommandResult;
import com.hokage.ssh.component.SshExecComponent;
import com.hokage.ssh.domain.FileProperty;
import com.hokage.ssh.enums.LsOptionEnum;
import com.hokage.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author yiminlin
 * @date 2021/06/03 11:46 pm
 * @description file management service implementation
 **/
@Slf4j
@Service
public class HokageFileManagementServiceImpl implements HokageFileManagementService {

    @Value("${file.management.preview.line}")
    private String previewLine;

    private HokageServerCacheDao serverCacheDao;
    private CommandDispatcher dispatcher;
    private SshExecComponent execComponent;
    private SshProperty2WebProperty fileConverter;

    @Autowired
    public void setServerCacheDao(HokageServerCacheDao serverCacheDao) {
        this.serverCacheDao = serverCacheDao;
    }

    @Autowired
    public void setDispatcher(CommandDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Autowired
    public void setExecComponent(SshExecComponent execComponent) {
        this.execComponent = execComponent;
    }

    @Autowired
    public void setFileConverter(SshProperty2WebProperty fileConverter) {
        this.fileConverter = fileConverter;
    }


    @Override
    public ServiceResponse<HokageFileVO> list(String serverKey, String dir, List<LsOptionEnum> options) {
        ServiceResponse<HokageFileVO> response = new ServiceResponse<>();
        Optional<SshClient> optional = serverCacheDao.get(serverKey);
        if (!optional.isPresent()) {
            return response.fail(ResultCodeEnum.SERVER_NO_FOUND.getCode(), ResultCodeEnum.SERVER_NO_FOUND.getMsg());
        }
        SshClient client = optional.get();
        try {
            AbstractCommand command = dispatcher.dispatch(client);
            CommandResult lsResult = execComponent.execute(client, command.ls(dir, options));
            CommandResult pwdResult = execComponent.execute(client, command.pwd(dir));
            if (!lsResult.isSuccess() || !pwdResult.isSuccess()) {
                String errMsg= String.format("exiStatus: %s, msg: %s", lsResult.getExitStatus(), lsResult.getMsg());
                return response.fail(ResultCodeEnum.COMMAND_EXECUTED_FAILED.getCode(), errMsg);
            }
            return response.success(assembleFileVO(lsResult, pwdResult));
        } catch (Exception e) {
            log.error("HokageFileManagementServiceImpl.list failed. err: {}", e.getMessage());
            return response.fail(ResultCodeEnum.COMMAND_EXECUTED_FAILED.getCode(), e.getMessage());
        }
    }

    @Override
    public ServiceResponse<FileContentVO> open(String serverKey, String curDir) {
        ServiceResponse<FileContentVO> response = new ServiceResponse<>();
        Optional<SshClient> optional = serverCacheDao.get(serverKey);
        if (!optional.isPresent()) {
            return response.fail(ResultCodeEnum.SERVER_NO_FOUND.getCode(), ResultCodeEnum.SERVER_NO_FOUND.getMsg());
        }
        SshClient client = optional.get();
        try {
            AbstractCommand command = dispatcher.dispatch(client);
            CommandResult wcResult = execComponent.execute(client, command.wc(curDir));
            CommandResult previewResult = execComponent.execute(client, command.preview(curDir));

            if (!previewResult.isSuccess() || !wcResult.isSuccess()) {
                String errMsg= String.format("exiStatus: %s, msg: %s", previewResult.getExitStatus(), previewResult.getMsg());
                return response.fail(ResultCodeEnum.COMMAND_EXECUTED_FAILED.getCode(), errMsg);
            }
            return response.success(assembleFileContentVO(wcResult, previewResult, curDir));
        } catch (Exception e) {
            log.error("HokageFileManagementServiceImpl.cat failed. err: {}", e.getMessage());
            return response.fail(ResultCodeEnum.COMMAND_EXECUTED_FAILED.getCode(), e.getMessage());
        }
    }

    private HokageFileVO assembleFileVO(CommandResult lsResult, CommandResult pwdResult) {

        List<FileProperty> fileList = JSONArray.parseArray(lsResult.getContent(), FileProperty.class);

        List<HokageFileProperty> propertyList = fileList.stream().map(property -> fileConverter.doForward(property)).collect(Collectors.toList());
        String curDir = pwdResult.getContent();
        long directoryNum = propertyList.stream().filter(property -> StringUtils.equals(property.getType(), "directory")).count();
        long fileNum = propertyList.stream().filter(property -> StringUtils.equals(property.getType(), "file")).count();
        long totalSize = fileList.stream().mapToLong(FileProperty::getSize).sum();
        String readableSize = FileUtil.humanReadable(totalSize);

        HokageFileVO fileVO = new HokageFileVO();
        fileVO.setCurDir(curDir).setFileNum(fileNum).setDirectoryNum(directoryNum).setFilePropertyList(propertyList).setTotalSize(readableSize);

        return fileVO;
    }

    private FileContentVO assembleFileContentVO(CommandResult wcResult, CommandResult previewResult, String path) {

        int lastIndex = StringUtils.lastIndexOf(path, '/');
        String name = StringUtils.substring(path, lastIndex + 1);

        lastIndex = StringUtils.lastIndexOf(name, '.');
        String type = StringUtils.lowerCase(StringUtils.substring(name, lastIndex + 1));

        FileContentVO contentVO = new FileContentVO();
        contentVO.setName(name).setType(type)
                .setContent(previewResult.getContent())
                .setTotalLine(Long.parseLong(wcResult.getContent()))
                .setCurLine(Long.parseLong(previewLine));

        return contentVO;
    }
}
