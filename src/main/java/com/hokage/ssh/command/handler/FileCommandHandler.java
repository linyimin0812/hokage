package com.hokage.ssh.command.handler;

import com.alibaba.fastjson.JSONArray;
import com.hokage.biz.converter.file.SshProperty2WebProperty;
import com.hokage.biz.enums.ResultCodeEnum;
import com.hokage.biz.request.command.BaseCommandParam;
import com.hokage.biz.request.command.FileParam;
import com.hokage.biz.response.file.FileContentVO;
import com.hokage.biz.response.file.HokageFileProperty;
import com.hokage.biz.response.file.HokageFileVO;
import com.hokage.common.ServiceResponse;
import com.hokage.ssh.SshClient;
import com.hokage.ssh.command.AbstractCommand;
import com.hokage.ssh.command.CommandDispatcher;
import com.hokage.ssh.command.CommandResult;
import com.hokage.ssh.component.SshExecComponent;
import com.hokage.ssh.component.SshSftpComponent;
import com.hokage.ssh.domain.FileProperty;
import com.hokage.util.FileUtil;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * @author yiminlin
 * @date 2021/06/19 7:21 pm
 * @description file management handler
 **/
@Slf4j
@Component
public class FileCommandHandler<T extends BaseCommandParam> {

    @Value("${file.management.preview.line}")
    private String previewLine;

    private CommandDispatcher dispatcher;
    private SshExecComponent execComponent;
    private SshSftpComponent sftpComponent;
    private SshProperty2WebProperty fileConverter;

    @Autowired
    public void setDispatcher(CommandDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Autowired
    public void setExecComponent(SshExecComponent execComponent) {
        this.execComponent = execComponent;
    }

    @Autowired
    public void setSftpComponent(SshSftpComponent sftpComponent) {
        this.sftpComponent = sftpComponent;
    }

    @Autowired
    public void setFileConverter(SshProperty2WebProperty fileConverter) {
        this.fileConverter = fileConverter;
    }

    public BiFunction<SshClient, T, ServiceResponse<HokageFileVO>> listHandler = ((client, param) -> {
        ServiceResponse<HokageFileVO> response = new ServiceResponse<>();
        FileParam fileParam = (FileParam) param;
        try {
            AbstractCommand command = dispatcher.dispatch(client);
            CommandResult lsResult = execComponent.execute(client, command.ls(fileParam.getPath(), param.getOptions()));
            CommandResult pwdResult = execComponent.execute(client, command.pwd(fileParam.getPath()));
            if (!lsResult.isSuccess() || !pwdResult.isSuccess()) {
                String errMsg= String.format("exiStatus: %s, msg: %s", lsResult.getExitStatus(), lsResult.getMsg());
                return response.fail(ResultCodeEnum.COMMAND_EXECUTED_FAILED.getCode(), errMsg);
            }
            return response.success(assembleFileVO(lsResult, pwdResult));
        } catch (Exception e) {
            log.error("HokageFileManagementServiceImpl.list failed. err: {}", e.getMessage());
            return response.fail(ResultCodeEnum.COMMAND_EXECUTED_FAILED.getCode(), e.getMessage());
        }
    });

    public BiFunction<SshClient, T, ServiceResponse<FileContentVO>> openHandler = (((client, t) -> {
        ServiceResponse<FileContentVO> response = new ServiceResponse<>();

        FileParam param = (FileParam) t;
        try {
            AbstractCommand command = dispatcher.dispatch(client);
            CommandResult wcResult = execComponent.execute(client, command.wc(param.getPath()));
            CommandResult previewResult = execComponent.execute(client, command.preview(param.getPath(), param.getPage()));

            if (!previewResult.isSuccess() || !wcResult.isSuccess()) {
                String errMsg= String.format("exiStatus: %s, msg: %s", previewResult.getExitStatus(), previewResult.getMsg());
                return response.fail(ResultCodeEnum.COMMAND_EXECUTED_FAILED.getCode(), errMsg);
            }
            return response.success(assembleFileContentVO(wcResult, previewResult, param.getPath()));
        } catch (Exception e) {
            log.error("HokageFileManagementServiceImpl.cat failed. err: {}", e.getMessage());
            return response.fail(ResultCodeEnum.COMMAND_EXECUTED_FAILED.getCode(), e.getMessage());
        }
    }));

    public BiFunction<SshClient, T, ServiceResponse<Boolean>> rmHandler = ((client, t) -> {
        ServiceResponse<Boolean> response = new ServiceResponse<>();
        FileParam param = (FileParam) t;
        try {
            AbstractCommand command = dispatcher.dispatch(client);
            CommandResult rmResult = execComponent.execute(client, command.rm(param.getPath()));
            if (!rmResult.isSuccess()) {
                String errMsg= String.format("exiStatus: %s, msg: %s", rmResult.getExitStatus(), rmResult.getMsg());
                return response.fail(ResultCodeEnum.COMMAND_EXECUTED_FAILED.getCode(), errMsg);
            }
            return response.success(Boolean.TRUE);
        } catch (Exception e) {
            log.error("HokageFileManagementServiceImpl.rm failed. err: {}", e.getMessage());
            return response.fail(ResultCodeEnum.COMMAND_EXECUTED_FAILED.getCode(), e.getMessage());
        }
    });

    public BiFunction<SshClient, T, ServiceResponse<Boolean>> downloadHandler = ((client, t) -> {
        ServiceResponse<Boolean> response = new ServiceResponse<>();
        FileParam param = (FileParam) t;
        try {
            sftpComponent.download(client, param.getPath(), param.getOs());
            return response.success(Boolean.TRUE);
        } catch (Exception e) {
            log.error("HokageFileManagementServiceImpl.download failed. err: {}", e.getMessage());
            return response.fail(ResultCodeEnum.FILE_DOWNLOAD_FAILED.getCode(), e.getMessage());
        } finally {
            if (Objects.nonNull(param.getOs())) {
                try {
                    param.getOs().close();
                } catch (IOException ignored) { }
            }
        }
    });

    public BiFunction<SshClient, T, ServiceResponse<Boolean>> tarHandler = ((client, t) -> {
        ServiceResponse<Boolean> response = new ServiceResponse<>();
        FileParam param = (FileParam) t;
        try {
            CommandResult rmResult = execComponent.execute(client, AbstractCommand.tar(param.getPath()));
            if (!rmResult.isSuccess()) {
                String errMsg= String.format("exiStatus: %s, msg: %s", rmResult.getExitStatus(), rmResult.getMsg());
                return response.fail(ResultCodeEnum.COMMAND_EXECUTED_FAILED.getCode(), errMsg);
            }
            return response.success(Boolean.TRUE);
        } catch (Exception e) {
            log.error("HokageFileManagementServiceImpl.tar failed. err: {}", e.getMessage());
            return response.fail(ResultCodeEnum.FILE_TAR_FAILED.getCode(), e.getMessage());
        }
    });

    public BiFunction<SshClient, T, ServiceResponse<Boolean>> uploadHandler = ((client, t) -> {
        ServiceResponse<Boolean> response = new ServiceResponse<>();
        FileParam param = (FileParam) t;
        try {
            sftpComponent.upload(client, param.getDstPath(), param.getSrc());
            return response.success(Boolean.TRUE);
        } catch (Exception e) {
            log.error("HokageFileManagementServiceImpl.upload failed. err: {}", e.getMessage());
            return response.fail(ResultCodeEnum.FILE_UPLOAD_FAILED.getCode(), e.getMessage());
        } finally {
            if (Objects.nonNull(client)) {
                Session session = client.getSessionIfPresent();
                if (Objects.nonNull(session)) {
                    session.disconnect();
                }
            }
        }
    });

    public BiFunction<SshClient, T, ServiceResponse<Boolean>> moveHandler = ((client, t) -> {
        ServiceResponse<Boolean> response = new ServiceResponse<>();
        FileParam param = (FileParam) t;
        try {
            CommandResult moveResult = execComponent.execute(client, AbstractCommand.move(param.getSrcPath(), param.getDstPath()));
            if (!moveResult.isSuccess()) {
                String errMsg= String.format("exiStatus: %s, msg: %s", moveResult.getExitStatus(), moveResult.getMsg());
                return response.fail(ResultCodeEnum.COMMAND_EXECUTED_FAILED.getCode(), errMsg);
            }
            return response.success(Boolean.TRUE);
        } catch (Exception e) {
            log.error("HokageFileManagementServiceImpl.move failed. err: {}", e.getMessage());
            return response.fail(ResultCodeEnum.FILE_TAR_FAILED.getCode(), e.getMessage());
        }
    });

    public BiFunction<SshClient, T, ServiceResponse<Boolean>> chmodHandler = ((client, t) -> {
        ServiceResponse<Boolean> response = new ServiceResponse<>();
        FileParam param = (FileParam) t;
        try {
            CommandResult moveResult = execComponent.execute(client, AbstractCommand.chmod(param.getPath(), param.getPermission()));
            if (!moveResult.isSuccess()) {
                String errMsg= String.format("exiStatus: %s, msg: %s", moveResult.getExitStatus(), moveResult.getMsg());
                return response.fail(ResultCodeEnum.COMMAND_EXECUTED_FAILED.getCode(), errMsg);
            }
            return response.success(Boolean.TRUE);
        } catch (Exception e) {
            log.error("HokageFileManagementServiceImpl.chmod failed. err: {}", e.getMessage());
            return response.fail(ResultCodeEnum.FILE_TAR_FAILED.getCode(), e.getMessage());
        }
    });


    private HokageFileVO assembleFileVO(CommandResult lsResult, CommandResult pwdResult) {

        List<FileProperty> fileList = JSONArray.parseArray(lsResult.getContent(), FileProperty.class);
        String curDir = pwdResult.getContent();

        List<HokageFileProperty> propertyList = fileList.stream().map(property -> {
            HokageFileProperty fileProperty = fileConverter.doForward(property);
            fileProperty.setCurDir(curDir);
            return fileProperty;
        }).collect(Collectors.toList());

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
        String curDir = StringUtils.substring(path, 0, lastIndex);

        lastIndex = StringUtils.lastIndexOf(name, '.');
        String type = StringUtils.lowerCase(StringUtils.substring(name, lastIndex + 1));

        FileContentVO contentVO = new FileContentVO();
        contentVO.setName(name)
                .setCurDir(curDir)
                .setExtension(type)
                .setContent(previewResult.getContent())
                .setTotalLine(Long.parseLong(wcResult.getContent()))
                .setPerPageLine(Long.parseLong(previewLine));

        return contentVO;
    }

}
