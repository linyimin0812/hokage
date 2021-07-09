package com.hokage.ssh.command;

import com.hokage.ssh.enums.LsOptionEnum;
import com.hokage.ssh.enums.OsTypeEnum;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yiminlin
 * @date 2021/05/28 2:45 am
 * @description darwin command
 **/
@Component
public class DarwinCommand extends AbstractCommand {
    @Override
    public OsTypeEnum os() {
        return OsTypeEnum.darwin;
    }

    @Override
    public String ls(String dir, List<String> optionList) {
        return null;
    }

    @Override
    public String memInfo() {
        return null;
    }

    @Override
    public String bandwidth() {
        return null;
    }

    @Override
    public String process() {
        return null;
    }

    @Override
    public String downloadTransferRate() {
        return null;
    }

    @Override
    public String pwd(String dir) {
        return null;
    }

    @Override
    public String cpuInfo() {
        return null;
    }

    @Override
    public String lastLog() {
        return null;
    }

    @Override
    public String df() {
        return null;
    }

    @Override
    public String arp() {
        return null;
    }

    @Override
    public String netstat() {
        return null;
    }

    @Override
    public String addUser(String account, String passwd) {
        return null;
    }

    @Override
    public String report() {
        return null;
    }
}
