package com.hokage.ssh.component;

import com.hokage.ssh.SshClient;
import com.hokage.ssh.command.CommandDispatcher;
import com.hokage.ssh.enums.JSchChannelType;
import com.hokage.ssh.enums.OsTypeEnum;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author yiminlin
 * @date 2021/05/27 1:51 上午
 * @description jsch exec channel component for execute admin' command
 **/
@Component
public class SshExecComponent {
    public String execute(SshClient client, String command) throws Exception {
        Session session = client.getSession();
        ChannelExec exec = (ChannelExec) session.openChannel(JSchChannelType.EXEC.getValue());
        exec.setPty(false);
        exec.setInputStream(null);
        exec.connect();

        BufferedReader reader = new BufferedReader(new InputStreamReader(exec.getInputStream(), StandardCharsets.UTF_8));
        BufferedReader errReader = new BufferedReader(new InputStreamReader(exec.getErrStream(), StandardCharsets.UTF_8));
        return "";
    }
}
