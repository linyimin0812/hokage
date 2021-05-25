package com.hokage.ssh;

import com.hokage.ssh.enums.JSchChannelType;
import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Properties;

/**
 * @author linyimin
 * @date 2021/05/25 00:56 pm
 * @email linyimin520812@gmail.com
 * @description ssh client
 */
@Slf4j
public class SshClient {
    private final JSch jSch;
    private final SshContext context;
    private Session session;
    private ChannelShell shell;
    private ChannelExec exec;

    public SshClient(SshContext context) throws JSchException {
        this.jSch = new JSch();
        this.context = context;
        this.sessionConfig();
    }

    private void sessionConfig() throws JSchException {
        Properties config = new Properties();
        // jsch的特性,如果没有这个配置,无法完成ssh连接
        config.put("StrictHostKeyChecking", "no");
        config.put("PreferredAuthentications", "password");
        this.session = jSch.getSession(context.getAccount(), context.getIp(), Integer.parseInt(context.getSshPort()));
        this.session.setConfig(config);
        this.session.setPassword(context.getPasswd());
//        this.session.setServerAliveInterval(3 * 1000);
        this.session.connect(30 * 1000);
    }

    public Session getSession() throws JSchException {
        try {
            ChannelExec testChannel = (ChannelExec) this.session.openChannel(JSchChannelType.EXEC.getValue());
            testChannel.setCommand("true");
            testChannel.connect(30 * 1000);
            if (log.isDebugEnabled()) {
                log.debug("Session successfully tested, use it again.");
            }
            testChannel.disconnect();
        } catch (JSchException e) {
            log.info("Session terminated. Create a new one.");
            this.sessionConfig();
        }
        return session;
    }

    public ChannelShell getShell() throws JSchException {
        if (Objects.nonNull(shell) && (shell.isConnected() || shell.isClosed())) {
            return shell;
        }
        this.session = getSession();
        shell = (ChannelShell) this.session.openChannel(JSchChannelType.SHELL.getValue());
        shell.connect(30 * 1000);
        return shell;
    }

    public ChannelExec getExec() throws JSchException {
        if (Objects.nonNull(exec) && exec.isConnected()) {
            return exec;
        }
        this.session = getSession();
        exec = (ChannelExec) this.session.openChannel(JSchChannelType.SHELL.getValue());
        exec.connect();
        return exec;
    }

    public SshContext getContext() {
        return context;
    }

}
