package com.hokage.ssh.component;

import com.google.common.base.Stopwatch;
import com.hokage.ssh.SshClient;
import com.hokage.ssh.command.CommandResult;
import com.hokage.ssh.enums.JSchChannelType;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author yiminlin
 * @date 2021/05/27 1:51 am
 * @description jsch exec channel component for execute admin' command
 **/
@Slf4j
@Component
public class SshExecComponent {

    /**
     * command execute timeout
     */
    private final static Integer TIME_OUT = 30 * 1000;

    public CommandResult execute(SshClient client, String command) throws Exception {

        ChannelExec exec = null;
        InputStream in = null;
        InputStream err = null;
        Stopwatch stopwatch = null;

        try {
            Session session = client.getSessionOrCreate();
            exec = (ChannelExec) session.openChannel(JSchChannelType.EXEC.getValue());

            exec.setPty(false);
            exec.setCommand(command);
            in = exec.getInputStream();
            err = exec.getErrStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            BufferedReader errReader = new BufferedReader(new InputStreamReader(err, StandardCharsets.UTF_8));

            String buf;
            StringBuilder sb = new StringBuilder();
            stopwatch =  Stopwatch.createStarted();
            exec.connect(TIME_OUT);

            while ((buf = reader.readLine()) != null || (buf = errReader.readLine()) != null) {
                sb.append(buf);
                if (stopwatch.elapsed(TimeUnit.MILLISECONDS) > TIME_OUT) {
                    log.warn("SshExecCommand.execute timeout. sshClient: {}, command: {}", client, command);
                    break;
                }
            }

            if (stopwatch.elapsed(TimeUnit.MILLISECONDS) > TIME_OUT) {
                return CommandResult.timeout(sb.toString(), exec.getExitStatus());
            }

            return CommandResult.success(sb.toString(), exec.getExitStatus());

        } catch (Exception e) {
            log.warn("SshExecCommand.execute error. sshClient: {}, command: {}, error: {}", client, command, e);
            return CommandResult.failed(e.getMessage(), Objects.isNull(exec) ? -1 : exec.getExitStatus());
        } finally {
            if (Objects.nonNull(exec)) {
                exec.disconnect();
            }
            if (Objects.nonNull(in)) {
                in.close();
            }
            if (Objects.nonNull(err)) {
                err.close();
            }
            if (Objects.nonNull(stopwatch)) {
                stopwatch.stop();
            }
        }
    }
}
