package com.hokage.ssh.command.handler;

import com.hokage.biz.enums.LoginTypeEnum;
import com.hokage.biz.enums.RecordStatusEnum;
import com.hokage.biz.enums.ResultCodeEnum;
import com.hokage.biz.request.command.AccountParam;
import com.hokage.biz.request.command.BaseCommandParam;
import com.hokage.common.ServiceResponse;
import com.hokage.persistence.dataobject.HokageSubordinateServerDO;
import com.hokage.ssh.SshClient;
import com.hokage.ssh.command.AbstractCommand;
import com.hokage.ssh.command.CommandDispatcher;
import com.hokage.ssh.command.result.CommandResult;
import com.hokage.ssh.component.SshExecComponent;
import com.hokage.ssh.context.SshContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.function.BiFunction;

/**
 * @author yiminlin
 * @date 2021/07/13 11:21 am
 * @description account command handler
 **/
@Slf4j
@Component
public class AccountCommandHandler<T extends BaseCommandParam> {
    private CommandDispatcher dispatcher;
    private SshExecComponent execComponent;

    @Autowired
    public void setDispatcher(CommandDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Autowired
    public void setExecComponent(SshExecComponent execComponent) {
        this.execComponent = execComponent;
    }

    public BiFunction<SshClient, T, ServiceResponse<HokageSubordinateServerDO>> addAccountHandler = ((client, param) -> {
        ServiceResponse<HokageSubordinateServerDO> response = new ServiceResponse<>();
        try {
            AccountParam accountParam = (AccountParam) param;
            AbstractCommand command = dispatcher.dispatch(client);
            CommandResult addAccountResult = execComponent.execute(client, command.addUser(accountParam.getAccount(), accountParam.getPasswd()));
            if (!addAccountResult.isSuccess()) {
                return response.fail(String.valueOf(addAccountResult.getExitStatus()), addAccountResult.getMsg());
            }

            return response.success(assembleSubServerDO(client.getSshContext(), accountParam));
        } catch (Exception e) {
            log.error("create account error. errMsg: {}", e.getMessage());
            return response.fail(ResultCodeEnum.CREATE_ACCOUNT_ERROR.getCode(), e.getMessage());
        }
    });

    public BiFunction<SshClient,T , ServiceResponse<Boolean>> deleteAccountHandler = ((client, param) -> {
        ServiceResponse<Boolean> response = new ServiceResponse<>();
        try {
            AccountParam accountParam = (AccountParam) param;
            AbstractCommand command = dispatcher.dispatch(client);
            CommandResult delAccountResult = execComponent.execute(client, command.delUser(accountParam.getAccount()));
            if (!delAccountResult.isSuccess()) {
                return response.fail(String.valueOf(delAccountResult.getExitStatus()), delAccountResult.getMsg());
            }

            return response.success(Boolean.TRUE);
        } catch (Exception e) {
            log.error("del account error. errMsg: {}", e.getMessage());
            return response.fail(ResultCodeEnum.DELETE_ACCOUNT_ERROR.getCode(), e.getMessage());
        }
    });

    private HokageSubordinateServerDO assembleSubServerDO(SshContext context, AccountParam param) {
        HokageSubordinateServerDO subServerDO = new HokageSubordinateServerDO();
        subServerDO.setServerId(context.getId());
        subServerDO.setAccount(param.getAccount());
        subServerDO.setPasswd(param.getPasswd());
        subServerDO.setIp(context.getIp());
        subServerDO.setLoginType(LoginTypeEnum.password.getValue());
        subServerDO.setSshPort(context.getSshPort());
        subServerDO.setStatus(RecordStatusEnum.inuse.getStatus());
        return subServerDO;
    }
}
