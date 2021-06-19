package com.hokage.biz.service;

import com.hokage.biz.enums.ResultCodeEnum;
import com.hokage.biz.service.HokageCommandService;
import com.hokage.cache.HokageServerCacheDao;
import com.hokage.common.ServiceResponse;
import com.hokage.ssh.SshClient;
import com.hokage.ssh.command.CommandDispatcher;
import com.hokage.ssh.command.handler.MonitorCommandHandler;
import com.hokage.ssh.component.SshExecComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

/**
 * @author yiminlin
 * @date 2021/05/28 11:37 pm
 * @description command service interface implementation
 **/
@Service
public abstract class AbstractCommandService {
    private HokageServerCacheDao serverCacheDao;

    @Autowired
    public void setServerCacheDao(HokageServerCacheDao serverCacheDao) {
        this.serverCacheDao = serverCacheDao;
    }

    /**
     * execute command and encapsulates result
     * @param serverKey ip_sshPort_account
     * @param commandHandler command handler
     * @param <R> type of command result
     * @return command result
     */
    public <R> ServiceResponse<R> execute(String serverKey, Function<SshClient, ServiceResponse<R>> commandHandler) {
        ServiceResponse<R> response = new ServiceResponse<>();
        Optional<SshClient> optional = serverCacheDao.getExecClient(serverKey);
        if (!optional.isPresent()) {
            return response.fail(ResultCodeEnum.SERVER_NO_FOUND.getCode(), ResultCodeEnum.SERVER_NO_FOUND.getMsg());
        }

        return commandHandler.apply(optional.get());
    }
}
