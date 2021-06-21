package com.hokage.ssh.command.result;

import lombok.Data;

/**
 * @author yiminlin
 * @date 2021/06/21 11:13 pm
 * @description memory stat
 **/
@Data
public class MemoryStat {
    private Long total;
    private Long used;
    private Long free;
}
