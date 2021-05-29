package com.hokage.ssh;

import com.hokage.ssh.context.ChannelShellContext;
import com.hokage.ssh.context.SshContext;
import com.hokage.ssh.enums.JSchChannelType;
import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
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
    private JSch jSch;
    private SshContext context;
    private Session session;
    private ChannelShellContext shellContext;

    public SshClient(SshContext context) throws Exception {
        this(context, false);
    }

    public SshClient(SshContext context, boolean ignored) throws Exception {
        this.context = context;
        if (!ignored) {
            if (!checkServerReachable(context)) {
                throw new RuntimeException(String.format("ip: %s, port: %s is unreachable.", context.getIp(), context.getSshPort()));
            }
            this.jSch = new JSch();
            this.sessionConfig();
        }
    }

    private void sessionConfig() throws JSchException {
        Properties config = new Properties();
        // jsch的特性,如果没有这个配置,无法完成ssh连接
        config.put("StrictHostKeyChecking", "no");
        config.put("PreferredAuthentications", "password");
        this.session = jSch.getSession(context.getAccount(), context.getIp(), Integer.parseInt(context.getSshPort()));
        this.session.setConfig(config);
        this.session.setPassword(context.getPasswd());
        this.session.connect(30 * 1000);
    }

    public Session getSessionOrCreate() throws JSchException {
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

    public Session getSession() {
        return this.session;
    }

    public ChannelShellContext getShellContext() throws Exception {
        if (Objects.isNull(shellContext)) {
            this.session = getSessionOrCreate();
            ChannelShell shell = (ChannelShell) this.session.openChannel(JSchChannelType.SHELL.getValue());
            shell.connect(30 * 1000);
            // set terminal window dimension
            shell.setPtySize(context.getSize().getCols(), context.getSize().getRows(), 640, 480);
            this.shellContext = new ChannelShellContext(shell);
            return shellContext;
        }
        return shellContext;
    }

    public ChannelShell getShell() throws Exception {
        return getShellContext().getShell();
    }

    public SshContext getContext() {
        return context;
    }

    /**
     * check the ssh socket reachable
     * @param context ssh context
     * @return is reachable: true/false
     * @throws IOException
     */
    private boolean checkServerReachable(SshContext context) throws IOException {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(context.getIp(), Integer.parseInt(context.getSshPort())), 5 * 1000);
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (socket.isConnected()) {
                socket.close();
            }
        }
    }
}
