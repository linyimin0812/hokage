package com.hokage.ssh.command.result;

import lombok.Data;

/**
 * @author yiminlin
 * @date 2021/06/21 11:08 pm
 * @description load average stat
 **/
@Data
public class LoadAverageStat {
    private Float oneMinAverage;
    private Float fiveMinAverage;
    private Float fifteenMinAverage;
}
