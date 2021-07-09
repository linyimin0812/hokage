package com.hokage.ssh.command;

import com.hokage.biz.Constant;
import com.hokage.util.FileUtil;
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
        return command.replace("${dir}", FileUtil.escapeNameWithSingleQuote(dir))
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
        return command.replace("${dir}", FileUtil.escapeNameWithSingleQuote(path));
    }

    /**
     * remove file or directory
     * @param path file or directory path, may be relative path or absolute path
     * @return rm command
     */
    public String rm(String path) {
        return String.format("rm -r %s;", FileUtil.escapeNameWithSingleQuote(path));
    }

    /**
     * package folder
     * @param path folder path
     */
    public static String tar(String path) {
        int lastIndex = StringUtils.lastIndexOf(path, '/');
        String dir = FileUtil.escapeNameWithSingleQuote(StringUtils.substring(path, 0, lastIndex));
        String fileName = FileUtil.escapeNameWithSingleQuote(StringUtils.substring(path,  lastIndex+ 1));
        String tarFileName = FileUtil.escapeNameWithSingleQuote(fileName + ".tar.gz");

        return String.format("cd %s; tar -zcvf %s %s;", dir, tarFileName, fileName);
    }

    /**
     * move folder or file
     * @param src file or directory which is moved from
     * @param dst file or directory which is moved to
     */
    public static String move(String src, String dst) {
        return String.format("mv %s %s;", FileUtil.escapeNameWithSingleQuote(src), FileUtil.escapeNameWithSingleQuote(dst));
    }

    /**
     * change file or directory permission
     * @param curDir file path whose permission is changed
     * @param permission file or directory permission
     */
    public static String chmod(String curDir, String permission) {
        return String.format("chmod -R %s %s;", permission, FileUtil.escapeNameWithSingleQuote(curDir));
    }

    public static String accountInfo() {
        return "awk -F: 'BEGIN {print \"[\"} " +
                "{if($3>100){userType=\"user\";} " +
                "else if($3>=1){userType=\"admin\";} " +
                "else {userType=\"root\";} " +
                "print \"{\\\"type\\\": \\\"\"userType\"\\\", " +
                "\\\"account\\\": \\\"\"$1\"\\\", " +
                "\\\"description\\\": \\\"\"$5\"\\\", " +
                "\\\"home\\\": \\\"\"$6\"\\\"},\"} " +
                "END {print \"]\"}' < /etc/passwd" + " | " +
                "sed 'N;$s/},/}/;P;D';";
    }

    public static String kill(Long pid) {
        return String.format("kill %s", pid);
    }

    public static String general() {
        return String.format("bash %s/%s/%s general_info", HOME, Constant.WORK_HOME, Constant.LINUX_API_FILE);
    }

    public static String interfaceIp() {
        return String.format("bash %s/%s/%s interface_ip", HOME, Constant.WORK_HOME, Constant.LINUX_API_FILE);
    }

    public static String systemStat() {
        return String.format("bash %s/%s/%s system_status", HOME, Constant.WORK_HOME, Constant.LINUX_API_FILE);
    }
}
