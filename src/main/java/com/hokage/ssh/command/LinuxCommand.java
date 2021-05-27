package com.hokage.ssh.command;

import com.hokage.ssh.enums.LsOptionEnum;
import com.hokage.ssh.enums.OsTypeEnum;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author yiminlin
 * @date 2021/05/27 12:36 上午
 * @description  linux command
 **/
@Component
public class LinuxCommand extends AbstractCommand {
    @Override
    OsTypeEnum os() {
        return OsTypeEnum.linux;
    }

    @Override
    String ls(String dir, List<LsOptionEnum> optionList) {
        dir = ObjectUtils.defaultIfNull(dir, HOME);
        String option = CollectionUtils.isEmpty(optionList) ? StringUtils.EMPTY : "-" + StringUtils.join(optionList, "");
        return ("ls -al --time-style=full ${option} ${dir}" + " | " +
                "awk 'BEGIN {print \"[\"} NR>1 {print \"{" +
                "\\\"typeAndPermission\\\": \\\"\"$1\"\\\"," +
                "\\\"owner\\\": \\\"\"$3\"\\\"," +
                "\\\"group\\\": \\\"\"$4\"\\\"," +
                "\\\"size\\\": \\\"\"$5\"\\\"," +
                "\\\"lastAccessTime\\\": \\\"\"$6\" \"$7\"\\\"," +
                "\\\"name\\\": \\\"\"$9\"\\\"},\"}" +
                "END {print \"]\"}'" + " | " +
                "sed 'N;$s/},/}/;P;D'")
                .replace("${option}", option)
                .replace("${dir}", dir);
    }

    public static void main(String[] args) {
        LinuxCommand command = new LinuxCommand();
        System.out.println(command.ls());
    }


}
