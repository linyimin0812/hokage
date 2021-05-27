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
    public String ls(String dir, List<LsOptionEnum> optionList) {
        return null;
    }
}
