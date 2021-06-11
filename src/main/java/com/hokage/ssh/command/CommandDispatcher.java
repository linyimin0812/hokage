package com.hokage.ssh.command;

import com.hokage.biz.Constant;
import com.hokage.ssh.SshClient;
import com.hokage.ssh.component.SshExecComponent;
import com.hokage.ssh.enums.OsTypeEnum;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author yiminlin
 * @date 2021/05/28 2:02 am
 * @description command dispatcher
 **/
@Slf4j
@Component
public class CommandDispatcher implements ApplicationContextAware {

    private SshExecComponent component;
    private Map<OsTypeEnum, Command> osMapCommand;
    @Getter
    private Map<OsTypeEnum, String> osMapScript;

    @Autowired
    public void setComponent(SshExecComponent component) {
        this.component = component;
    }

    @PostConstruct
    public void init() throws IOException {
        osMapScript = new ConcurrentHashMap<>(2);
        osMapScript.put(OsTypeEnum.linux, readShellCommand(Constant.LINUX_API_FILE));
        osMapScript.put(OsTypeEnum.darwin, readShellCommand(Constant.DARWIN_API_FILE));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        osMapCommand = applicationContext.getBeansOfType(Command.class)
                .values().stream()
                .collect(Collectors.toMap(Command::os, Function.identity(), (o1, o2) -> o1));
    }

    public AbstractCommand dispatch(SshClient client) throws Exception {
        OsTypeEnum os = acquireOsType(client);
        if (!osMapCommand.containsKey(os)) {
            throw new RuntimeException("unsupported os type: " + os);
        }
        return (AbstractCommand) osMapCommand.get(os);
    }

    public String dispatchScript(SshClient client) throws Exception {
        OsTypeEnum os = acquireOsType(client);
        if (!osMapScript.containsKey(os)) {
            throw new RuntimeException("unsupported os type: " + os);
        }
        return osMapScript.get(os);
    }

    private OsTypeEnum acquireOsType(SshClient client) throws Exception {
        CommandResult execResult = component.execute(client, AbstractCommand.uname());
        if (!execResult.isSuccess()) {
            throw new RuntimeException(String.format("execute uname error. exitStatus: %s, error: %s", execResult.getExitStatus(), execResult.getMsg()));
        }
        OsTypeEnum os = OsTypeEnum.parse(execResult.getContent());
        if (Objects.isNull(os)) {
            throw new RuntimeException("os type can't be empty.");
        }
        return os;
    }

    private String readShellCommand(String path) throws IOException {
        InputStream in = getClass().getResourceAsStream(path);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String buff;
        while ((buff = reader.readLine()) != null) {
            sb.append(buff).append(StringUtils.LF);
        }
        log.info(StringUtils.chomp(sb.toString()));
        return StringUtils.chomp(sb.toString());
    }
}
