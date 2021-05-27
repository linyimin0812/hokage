package com.hokage.ssh.context;

import com.jcraft.jsch.ChannelShell;
import lombok.Data;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author linyimin
 * @date 2021/05/26 08:56 am
 * @email linyimin520812@gmail.com
 * @description jcsh channel shell context
 */
@Data
public class ChannelShellContext {
    private ChannelShell shell;
    private InputStream inputStream;

    public ChannelShellContext(ChannelShell shell) throws IOException {
        this.shell = shell;
        this.inputStream = shell.getInputStream();
    }
}
