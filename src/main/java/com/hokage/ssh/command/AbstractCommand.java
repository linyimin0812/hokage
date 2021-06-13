package com.hokage.ssh.command;

import org.apache.commons.lang3.StringUtils;
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
     * @param page: view the page content
     * @return file content
     */
    public String preview(String dir, Long page) {
        long startLine = (page - 1) * Long.parseLong(previewLine) + 1;
        long endLine = page * Long.parseLong(previewLine);
        String command = "sed -n \"${startLine},${endLine}p\" ${dir};";
        return command.replace("${dir}", dir)
                .replace("${startLine}", String.valueOf(startLine))
                .replace("${endLine}", String.valueOf(endLine));
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
        return String.format("rm -r %s;", path);
    }

    /**
     * package folder
     * @param path folder path
     */
    public static String tar(String path) {
        String fileName = StringUtils.substring(path, StringUtils.lastIndexOf(path, '/') + 1);
        String dir = StringUtils.substring(path, 0, StringUtils.lastIndexOf(path, '/'));
        return String.format("cd %s; tar -zcvf %s.tar.gz %s;", dir, fileName, fileName);
    }
}
