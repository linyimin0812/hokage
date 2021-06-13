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
 * @date 2021/05/27 12:36 am
 * @description  linux command
 **/
@Component
public class LinuxCommand extends AbstractCommand {
    @Override
    public OsTypeEnum os() {
        return OsTypeEnum.linux;
    }

    @Override
    public String ls(String dir, List<LsOptionEnum> optionList) {
        dir = ObjectUtils.defaultIfNull(dir, HOME);
        String option = CollectionUtils.isEmpty(optionList) ? StringUtils.EMPTY : "-" + StringUtils.join(optionList, "");
        return ("ls -al --time-style=full ${option} ${dir}" + " | " +
                "awk 'BEGIN {print \"[\"} NR>1 {printf \"{" +
                "\\\"typeAndPermission\\\": \\\"\"$1\"\\\"," +
                "\\\"owner\\\": \\\"\"$3\"\\\"," +
                "\\\"group\\\": \\\"\"$4\"\\\"," +
                "\\\"size\\\": \\\"\"$5\"\\\"," +
                "\\\"lastAccessTime\\\": \\\"\"$6\" \"$7\"\\\"," +
                "\\\"name\\\":\"; name=$9;for(i=10;i<=NF;i++) name=name\" \"$i; print \"\\\"\"name\"\\\"},\"}" +
                "END {print \"]\"}'" + " | " +
                "sed 'N;$s/},/}/;P;D';")
                .replace("${option}", option)
                .replace("${dir}", dir);
    }

    @Override
    public String memInfo() {
        return "cat /proc/meminfo" + " | " +
                "awk '{out=\"\"; for(i=1;i<=NF;i++) {out=out$i} print out;}'" + " | " +
                "awk -F: 'BEGIN {print \"{\"} {print \"\\\"\"$1\"\\\": \\\"\"$2\"\\\",\"} END {print \"}\"}'" + " | " +
                "sed 'N;$s/,\\n/\\n/;P;D';"
                ;
    }

    @Override
    public String bandwidth() {
        return "cat /proc/net/dev" + " | " +
                "awk -F: 'NR>2 {print $1\" \"$2}'" + " | " +
                "awk 'BEGIN {print \"[\"} " +
                "{print \"{" +
                "\\\"interface\\\": \\\"\"$1\"\\\", " +
                "\\\"rx\\\": \"$2\", " +
                "\\\"tx\\\": \"$10\"},\"} " +
                "END {print \"]\"}'" + " | " +
                "sed 'N;$s/},/}/;P;D';";
    }

    @Override
    public String process() {
        return "ps axo pid,user,pcpu,rss,vsz,lstart,stat,comm" + " | " +
                "awk 'BEGIN {print \"[\"} NR>1 {printf \"{" +
                "\\\"pid\\\": \"$1\", \\\"user\\\": \\\"\"$2\"\\\", " +
                "\\\"cpu\\\": \"$3\", \\\"rss\\\": \"$4\", " +
                "\\\"vsz\\\": \"$5\", \\\"started\\\": \\\"\"$6\" \"$7\" \"$8\" \"$9\" \"$10\"\\\", " +
                "\\\"stat\\\": \\\"\"$11\"\\\"," +
                "\\\"command\\\":\"; out=$12; for(i=13;i<=NF;i++) {out=out$i} print \"\\\"\"out\"\\\"},\";} " +
                "END {print \"]\"}'"+ " | " +
                "sed 'N;$s/},/}/;P;D';";
    }

    @Override
    public String downloadTransferRate() {
        return null;
    }

    @Override
    public String pwd(String dir) {
        return String.format("cd %s; pwd;", dir);
    }

    public static void main(String[] args) {
        LinuxCommand command = new LinuxCommand();
        System.out.println(command.ls());
        System.out.println(command.memInfo());
        System.out.println(command.bandwidth());
        System.out.println(command.process());
        System.out.println(command.preview("/root/.presto_history", 1L));
        System.out.println(command.wc("/root/.presto_history"));
    }
}
