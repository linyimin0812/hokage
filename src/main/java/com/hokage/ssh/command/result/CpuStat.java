package com.hokage.ssh.command.result;

import lombok.Data;

/**
 * @author yiminlin
 * @date 2021/06/21 11:14 pm
 * @description cpu stat
 **/
@Data
public class CpuStat {
    private String name;
    private Long user;
    private Long nice;
    private Long system;
    private Long idle;
    private Long ioWait;
    private Long irq;
    private Long softIrq;
    private Long steal;
    private Long guest;
    private Long guestNice;
}
