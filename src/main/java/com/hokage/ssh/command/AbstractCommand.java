package com.hokage.ssh.command;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Collections;

/**
 * @author yiminlin
 * @date 2021/05/27 12:28 am
 * @description abstract command, support darwin and linux
 **/
@Component
public abstract class AbstractCommand implements Command {

    public static final String HOME = "~";

    @Value("${file.management.preview.line}")
    private String previewLine;

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
    public static String uname() {
        return "uname -s" + " | " + "tr A-Z a-z" + " | " +
               "awk '$0 ~ /darwin/ {print \"darwin\"}" +
                    "$0 ~ /linux/ {print \"linux\"}" +
                    "$0 !~ /darwin|linux/ {print \"unknown\"}'";
    }

    /**
     * acquire file content
     * @param dir file path, may be relative path or absolute path
     * @return file content
     */
    public String preview(String dir) {
        String command = "head -n ${previewLine} ${dir};";
        return command.replace("${dir}", dir).replace("${previewLine}", previewLine);
    }

    /**
     * acquire file line number
     * @param path file path,  may be relative path or absolute path
     * @return file line number
     */
    public String wc(String path) {
        String command = "wc -l ${dir}" + " | awk '{print $1}';";
        return command.replace("${dir}", path);
    }

    /**
     * remove file or directory
     * @param path file or directory path, may be relative path or absolute path
     * @return
     */
    public String rm(String path) {
        return String.format("rm -rf %s;", path);
    }
}
