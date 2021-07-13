package com.hokage.ssh.command;

import com.hokage.biz.Constant;
import com.hokage.ssh.enums.OsTypeEnum;
import com.hokage.util.FileUtil;
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
    public String ls(String dir, List<String> optionList) {
        dir = ObjectUtils.defaultIfNull(dir, HOME);
        String option = CollectionUtils.isEmpty(optionList) ? StringUtils.EMPTY : "-" + StringUtils.join(optionList, "");
        return ("ls -al --time-style=full ${option} ${dir}" + " | " +
                "awk 'BEGIN {print \"[\"} NR>1 {printf \"{" +
                "\\\"typeAndPermission\\\": \\\"\"$1\"\\\"," +
                "\\\"owner\\\": \\\"\"$3\"\\\"," +
                "\\\"group\\\": \\\"\"$4\"\\\"," +
                "\\\"size\\\": \\\"\"$5\"\\\"," +
                "\\\"lastAccessTime\\\": \\\"\"$6\" \"$7\"\\\"," +
                "\\\"name\\\":\"; print \"\\\"\"substr($0, index($0,$7\" \"$8)+length($7\" \"$8)+1)\"\\\"},\"}" +
                "END {print \"]\"}'" + " | " +
                "sed 'N;$s/},/}/;P;D';")
                .replace("${option}", option)
                .replace("${dir}", FileUtil.escapeNameWithSingleQuote(dir));
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
        return "ps axo pid,user,pcpu,%mem,rss,vsz,lstart,stat,comm,command" + " | " +
                "grep -v \"ps axo\"" + " | " +
                "grep -v \"awk BEGIN\"" + " | " +
                "grep -v \"sed N;\"" + " | " +
                "awk 'BEGIN {print \"[\"} NR>1 {print \"{" +
                "\\\"pid\\\": \"$1\", " +
                "\\\"account\\\": \\\"\"$2\"\\\", " +
                "\\\"cpu\\\": \"$3\", " +
                "\\\"mem\\\": \"$4\", " +
                "\\\"rss\\\": \"$5\", " +
                "\\\"vsz\\\": \"$6\", " +
                "\\\"started\\\": \\\"\"$7\" \"$8\" \"$9\" \"$10\" \"$11\"\\\", " +
                "\\\"status\\\": \\\"\"$12\"\\\"," +
                "\\\"comm\\\": \\\"\"$13\"\\\"," +
                "\\\"command\\\": \\\"\"substr($0,index($0,$14))\"\\\"},\";} " +
                "END {print \"]\"}'" + " | " +
                "sed 'N;$s/},/}/;P;D';";
    }

    @Override
    public String downloadTransferRate() {
        return null;
    }

    @Override
    public String pwd(String dir) {
        return String.format("cd %s; pwd;", FileUtil.escapeNameWithSingleQuote(dir));
    }

    @Override
    public String cpuInfo() {
        return "lscpu | awk -F: 'BEGIN {print \"{\"} {print \"\\\"\"$1\"\\\": \\\"\"$2\"\\\",\"} END {print \"}\"}' | " +
                "sed 'N;$s/,\\n/\\n/;P;D';";
    }

    @Override
    public String lastLog() {
        return "lastlog -t 365 " + " | " +
                "awk 'BEGIN {print \"[\"} NR>1 { printf \"{" +
                "\\\"account\\\": \\\"\"$1\"\\\", " +
                "\\\"port\\\": \\\"\"$2\"\\\", " +
                "\\\"from\\\":\"; " +
                "if($3 ~ /^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}/){ " +
                "print \"\\\"\"$3\"\\\", \\\"latest\\\": \\\"\"substr($0, index($0, $4))\"\\\"},\"} " +
                "else print \"\\\"\\\", \\\"latest\\\": \\\"\"substr($0, index($0, $3))\"\\\"},\";} " +
                "END {print \"]\"}' " + " | " +
                "sed 'N;$s/},/}/;P;D';";
    }

    @Override
    public String df() {
        return "df -k" + " | " +
                "awk 'BEGIN {print \"[\"} NR>1 && $2 ~ /[0-9]+/ {print \"{" +
                "\\\"fileSystem\\\": \\\"\"$1\"\\\"," +
                "\\\"size\\\": \"$2\", " +
                "\\\"used\\\": \"$3\", " +
                "\\\"available\\\": \"$4\", " +
                "\\\"capacity\\\": \\\"\"$5\"\\\", " +
                "\\\"mounted\\\": \\\"\"$6\"\\\"}, \"} " +
                "END {print \"]\"}'" + " | " +
                "sed 'N;$s/},/}/;P;D';";
    }

    @Override
    public String arp() {
        return "arp | awk 'BEGIN {print \"[\"} NR>1 {print \"{" +
                "\\\"ip\\\": \\\"\"$1\"\\\", " +
                "\\\"hwType\\\": \\\"\"$2\"\\\", " +
                "\\\"mac\\\": \\\"\"$3\"\\\", " +
                "\\\"interfaceName\\\": \\\"\"$5\"\\\"},\"} " +
                "END {print \"]\"}'" + " | " +
                "sed 'N;$s/},/}/;P;D';";
    }

    @Override
    public String netstat() {
        return "netstat -natp 2> /dev/null " + " | " +
                "awk 'BEGIN {print \"[\"} NR>2 {print \"{" +
                "\\\"protocol\\\": \\\"\"$1\"\\\", " +
                "\\\"localIp\\\": \\\"\"$4\"\\\", " +
                "\\\"foreignIp\\\": \\\"\"$5\"\\\", " +
                "\\\"status\\\": \\\"\"$6\"\\\", " +
                "\\\"process\\\": \\\"\"$7\"\\\"},\"} END {print \"]\"}'" + " | " +
                "sed 'N;$s/},/}/;P;D';";
    }

    @Override
    public String addUser(String account, String passwd) {
        String command = "useradd ${account}; echo \"${account}:${passwd}\" | chpasswd;";
        return command.replace("${account}", account).replace("${passwd}", passwd);
    }

    @Override
    public String delUser(String account) {
        return String.format("userdel %s", account);
    }

    @Override
    public String report() {
        return String.format("bash %s/%s/%s", HOME, Constant.WORK_HOME, Constant.LINUX_REPORT_FILE);
    }

    public static void main(String[] args) {
        LinuxCommand command = new LinuxCommand();
        System.out.println(command.ls());
        System.out.println(command.memInfo());
        System.out.println(command.bandwidth());
        System.out.println(command.process());
        System.out.println(command.wc("/root/.presto_history"));
        System.out.println(command.cpuInfo());
        System.out.println(AbstractCommand.accountInfo());
        System.out.println(command.lastLog());
        System.out.println(command.df());
        System.out.println(command.addUser("linyimin", "lym130060"));
    }
}
