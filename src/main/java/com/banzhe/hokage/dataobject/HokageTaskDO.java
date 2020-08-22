package com.banzhe.hokage.dataobject;

import lombok.Data;

/**
 * @author linyimin
 * @date 2020/7/26 9:53 下午
 * @email linyimin520812@gmail.com
 * @description 批量任务信息
 */
@Data
public class HokageTaskDO {
    private Long id;            // 任务id
    private Long userId;        // 任务所属用户id
    private String taskName;    // 任务名称
    private Integer taskType;   // 任务类型: 0: 默认为shell
    private Integer execType;   // 执行类型: 0: 定时, 1: cron周期
    private Long execTime;      // 执行时间对应的时间戳
    private String execServers; // 执行服务器(服务器IP或者服务器分组)
    private String execCommand; // 执行命令
    private String description; // 任务描述
}
