package com.hokage.ssh.command.result;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author yiminlin
 * @date 2021/06/22 12:56 am
 * @description system stat
 **/
@Data
public class SystemStat {
    private Map<String, Long> uploadRate;
    private Map<String, Long> downloadRate;
    private LoadAverageStat loadAvg;
    private MemoryStat memStat;
    private List<CpuStat> preCpuStat;
    private List<CpuStat> curCpuStat;
}
