package com.hokage.ssh.command.result.stat;

import lombok.Data;

/**
 * @author yiminlin
 * @date 2021/06/21 11:05 pm
 * @description download metric
 **/
@Data
public class DownloadStat {
    private String name;
    private Long value;
}
