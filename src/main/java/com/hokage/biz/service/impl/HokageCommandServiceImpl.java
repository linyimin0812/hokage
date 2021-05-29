package com.hokage.biz.service.impl;

import com.hokage.biz.service.HokageCommandService;
import com.hokage.ssh.command.CommandDispatcher;
import com.hokage.ssh.component.SshExecComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yiminlin
 * @date 2021/05/28 11:37 pm
 * @description command service interface implementation
 **/
@Service
public class HokageCommandServiceImpl implements HokageCommandService {
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
}
