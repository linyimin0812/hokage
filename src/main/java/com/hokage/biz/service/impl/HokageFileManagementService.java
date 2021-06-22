package com.hokage.biz.service.impl;

import com.hokage.biz.enums.ResultCodeEnum;
import com.hokage.biz.request.command.BaseCommandParam;
import com.hokage.biz.service.AbstractCommandService;
import com.hokage.common.ServiceResponse;
import com.hokage.ssh.SshClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.BiFunction;

/**
 * @author yiminlin
 * @date 2021/06/03 11:46 pm
 * @description file management service implementation
 **/
@Slf4j
@Service
public class HokageFileManagementService extends AbstractCommandService {
    /**
     * execute command and encapsulates result
     * @param serverKey ip_sshPort_account
     * @param commandHandler command handler
     * @param <R> type of command result
     * @return command result
     */
    public <R, T extends BaseCommandParam> ServiceResponse<R> ExecuteSftpCommand(String serverKey, T param, BiFunction<SshClient, T, ServiceResponse<R>> commandHandler) {
        ServiceResponse<R> response = new ServiceResponse<>();
        Optional<SshClient> optional = getServerCacheDao().getSftpClient(serverKey);
        if (!optional.isPresent()) {
            return response.fail(ResultCodeEnum.SERVER_NO_FOUND.getCode(), ResultCodeEnum.SERVER_NO_FOUND.getMsg());
        }
        return commandHandler.apply(optional.get(), param);
    }
}
