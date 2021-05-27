package com.hokage.ssh.command;

import com.hokage.ssh.enums.LsOptionEnum;
import com.hokage.ssh.enums.OsTypeEnum;

import java.util.Collections;
import java.util.List;

/**
 * @author yiminlin
 * @date 2021/05/27 12:28 am
 * @description abstract command, support darwin and linux
 **/
public abstract class AbstractCommand {

public static final String HOME = "~";

    /**
     * specify os type
     * @return os type
     */
    abstract OsTypeEnum os();

    /**
     * list directory contents
     * @param dir directory to list
     * @param optionList command option
     * @return ls command execute result content
     */
    abstract String ls(String dir, List<LsOptionEnum> optionList);

    /**
     * a default ls command
     * @return default ls command execute result content
     */
    public String ls() {
        return ls(HOME, Collections.emptyList());
    }



    /**
     * acquire operating system type
     * @return os name: darwin, linux and unknown
     */
    public String uname() {
        return "uname -s" + " | " + "tr A-Z a-z" + " | " +
               "awk '$0 ~ /darwin/ {print \"darwin\"}" +
                    "$0 ~ /linux/ {print \"linux\"}" +
                    "$0 !~ /darwin|linux/ {print \"unknown\"}";
    }
}
