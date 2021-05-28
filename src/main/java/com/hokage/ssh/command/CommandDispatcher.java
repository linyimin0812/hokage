package com.hokage.ssh.command;

import com.hokage.ssh.SshClient;
import com.hokage.ssh.component.SshExecComponent;
import com.hokage.ssh.enums.OsTypeEnum;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author yiminlin
 * @date 2021/05/28 2:02 am
 * @description command dispatcher
 **/
@Component
public class CommandDispatcher implements ApplicationContextAware {

    private SshExecComponent component;
    private Map<OsTypeEnum, Command> osMapCommand;

    @Autowired
    public void setComponent(SshExecComponent component) {
        this.component = component;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        osMapCommand = applicationContext.getBeansOfType(Command.class)
                .values().stream()
                .collect(Collectors.toMap(Command::os, Function.identity(), (o1, o2) -> o1));
    }

    public AbstractCommand dispatch(SshClient client) throws Exception {
        CommandResult execResult = component.execute(client, AbstractCommand.uname());
        if (!execResult.isSuccess()) {
            throw new RuntimeException(String.format("execute uname error. exitStatus: %s, error: %s", execResult.getExitStatus(), execResult.getMsg()));
        }
        OsTypeEnum os = OsTypeEnum.parse(execResult.getContent());
        if (Objects.isNull(os)) {
            throw new RuntimeException("os type can't be empty.");
        }
        if (!osMapCommand.containsKey(os)) {
            throw new RuntimeException("unsupported os type: " + os);
        }
        return (AbstractCommand) osMapCommand.get(os);
    }
}
