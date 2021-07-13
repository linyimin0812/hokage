package com.hokage.biz.service.impl;

import com.hokage.biz.request.command.AccountParam;
import com.hokage.biz.service.AbstractCommandService;
import com.hokage.common.ServiceResponse;
import com.hokage.persistence.dataobject.HokageServerDO;
import com.hokage.persistence.dataobject.HokageSubordinateServerDO;
import com.hokage.ssh.command.handler.AccountCommandHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yiminlin
 * @date 2021/07/13 11:58 am
 * @description account service
 **/
@Slf4j
@Service
public class HokageAccountService extends AbstractCommandService {
    private AccountCommandHandler<AccountParam> commandHandler;

    @Autowired
    public void setCommandHandler(AccountCommandHandler<AccountParam> commandHandler) {
        this.commandHandler = commandHandler;
    }

    public ServiceResponse<HokageSubordinateServerDO> addAccount(String serverKey, AccountParam param) {
        return super.execute(serverKey, param, commandHandler.addAccountHandler);
    }
}
