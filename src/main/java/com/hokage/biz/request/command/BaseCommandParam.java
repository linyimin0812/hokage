package com.hokage.biz.request.command;

import com.hokage.ssh.SshClient;
import lombok.Data;

import java.util.List;

/**
 * @author yiminlin
 * @date 2021/06/19 7:49 pm
 * @description base command param
 **/
@Data
public class BaseCommandParam {
    private List<String> options;
}
