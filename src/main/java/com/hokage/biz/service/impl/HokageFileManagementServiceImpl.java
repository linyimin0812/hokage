package com.hokage.biz.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.hokage.biz.enums.ResultCodeEnum;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    private HokageServerCacheDao serverCacheDao;
    private CommandDispatcher dispatcher;
    private SshExecComponent execComponent;

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


    @Override
    public ServiceResponse<List<HokageFileVO>> list(String serverKey, String dir, List<LsOptionEnum> options) {
        ServiceResponse<List<HokageFileVO>> response = new ServiceResponse<>();
        Optional<SshClient> optional = serverCacheDao.get(serverKey);
        if (!optional.isPresent()) {
            return response.fail(ResultCodeEnum.SERVER_NO_FOUND.getCode(), ResultCodeEnum.SERVER_NO_FOUND.getMsg());
        }

        SshClient client = optional.get();
        CommandResult result = null;
        try {
            AbstractCommand command = dispatcher.dispatch(client);
            result = execComponent.execute(client, command.ls(dir, options));
            if (!result.isSuccess()) {
                String errMsg= String.format("exiStatus: %s, msg: %s", result.getExitStatus(), result.getMsg());
                return response.fail(ResultCodeEnum.COMMAND_EXECUTED_FAILED.getCode(), errMsg);
            }
        } catch (Exception e) {
            log.error("HokageFileManagementServiceImpl.list failed. err: {}", e.getMessage());
            return response.fail(ResultCodeEnum.COMMAND_EXECUTED_FAILED.getCode(), e.getMessage());
        }
        List<FileProperty> fileList = JSONArray.parseArray(result.getContent(), FileProperty.class);
        List<HokageFileVO> fileVOList = fileList.stream().map(property -> {
            HokageFileVO fileVO = new HokageFileVO();
            BeanUtils.copyProperties(property, fileVO);
            return fileVO;
        }).collect(Collectors.toList());

        return response.success(fileVOList);
    }
}
