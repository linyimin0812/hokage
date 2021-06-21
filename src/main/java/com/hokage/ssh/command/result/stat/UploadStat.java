package com.hokage.ssh.command.result.stat;

import lombok.Data;

/**
 * @author yiminlin
 * @date 2021/06/21 11:07 pm
 * @description upload stat
 **/
@Data
public class UploadStat {
    private String name;
    private Long value;
}
