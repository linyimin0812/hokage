package com.hokage.ssh.command;

import com.hokage.ssh.enums.LsOptionEnum;
import com.hokage.ssh.enums.OsTypeEnum;

import java.util.List;

/**
 * @author yiminlin
 * @date 2021/05/28 1:59 上午
 * @description common command definition
 **/
public interface Command {
    /**
     * specify os type
     * @return os type
     */
    OsTypeEnum os();

    /**
     * list directory contents
     * @param dir directory to list
     * @param optionList command option
     * @return ls command execute result content
     */
    String ls(String dir, List<LsOptionEnum> optionList);
}
