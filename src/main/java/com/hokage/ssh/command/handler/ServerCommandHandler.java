package com.hokage.ssh.command.handler;

import com.hokage.biz.enums.ResultCodeEnum;
import com.hokage.biz.request.command.BaseCommandParam;
import com.hokage.common.ServiceResponse;
import com.hokage.ssh.SshClient;
import com.hokage.ssh.command.AbstractCommand;
import com.hokage.ssh.command.result.CommandResult;
import com.hokage.ssh.component.SshExecComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.BiFunction;

/**
 * @author yiminlin
 * @date 2021/07/18 6:30 pm
 * @description server command handler
 **/
@Slf4j
@Component
public class ServerCommandHandler<T extends BaseCommandParam> {
    private SshExecComponent execComponent;

    @Autowired
    public void setExecComponent(SshExecComponent execComponent) {
        this.execComponent = execComponent;
    }

    public BiFunction<SshClient, T, ServiceResponse<String>> hostnameHandler = ((client, param) -> {
        ServiceResponse<String> response = new ServiceResponse<>();
        try {
            CommandResult hostnameResult = execComponent.execute(client, AbstractCommand.hostname());
            if (!hostnameResult.isSuccess()) {
                String errMsg= String.format("exiStatus: %s, msg: %s", hostnameResult.getExitStatus(), hostnameResult.getMsg());
                return response.fail(ResultCodeEnum.COMMAND_EXECUTED_FAILED.getCode(), errMsg);
            }
            return response.success(hostnameResult.getContent());
        } catch (Exception e) {
            log.error("HokageFileManagementServiceImpl.list failed. err: {}", e.getMessage());
            return response.fail(ResultCodeEnum.COMMAND_EXECUTED_FAILED.getCode(), e.getMessage());
        }
    });
}
