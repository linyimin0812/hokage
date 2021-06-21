package com.hokage.ssh.command.result;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author yiminlin
 * @date 2021/05/28 11:11 pm
 * @description command execute result
 **/
@Data
@Accessors(chain = true)
public class CommandResult {
    private boolean success;
    private String content;
    private String msg;
    private Integer exitStatus;

    public static CommandResult success(String content, Integer exitStatus) {
        CommandResult result = new CommandResult();
        result.setSuccess(true).setContent(content).setExitStatus(exitStatus);
        return result;
    }

    public static CommandResult failed(String msg, Integer exitStatus) {
        CommandResult result = new CommandResult();
        result.setSuccess(false).setMsg(msg).setExitStatus(exitStatus);
        return result;
    }

    public static CommandResult timeout(String content, Integer exitStatus) {
        CommandResult result = new CommandResult();
        result.setSuccess(false).setContent(content).setMsg("timeout").setExitStatus(exitStatus);
        return result;
    }

}
