package com.banzhe.hokage.persistence.dataobject;

import lombok.Data;

/**
 * @author linyimin
 * @date 2020/7/26 9:59 下午
 * @email linyimin520812@gmail.com
 * @description 批量任务执行结果表
 */

@Data
public class HokageTaskResultDO extends HokageBaseDO {
    private Long id;            // 任务执行结果id
    private Long taskId;        // 任务id
    private Integer taskStatus; // 任务执行状态: 0: 执行完成, 1: 正在执行, 2: 未知
    private Long startTime;     // 任务开始执行的时间戳
    private Long endTime;       // 任务执行结束时间戳
    private Integer exitCode;   // 任务返回状态码
    private String execServer;  // 任务执行机器ip
    private String result;      // 执行返回内容
}
