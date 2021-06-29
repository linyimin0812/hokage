package com.hokage.persistence.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author linyimin
 * @date 2020/7/26 9:59 pm
 * @email linyimin520812@gmail.com
 * @description bat command execution result
 */

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class HokageTaskResultDO extends HokageBaseDO {
    /**
     * task result primary id
     */
    private Long id;
    /**
     * task primary id
     */
    private Long taskId;
    /**
     * task status: 0-running, 1-finished, 2-failed
     */
    private Integer taskStatus;

    /**
     * trigger status: 0-manual， 1-auto scheduled
     */
    private Integer triggerType;
    /**
     * task execute start timestamp
     */
    private Long startTime;
    /**
     * task execute end timestamp
     */
    private Long endTime;
    /**
     * task exit code
     */
    private Integer exitCode;
    /**
     * execute server, account@ip
     */
    private String execServer;
    /**
     * execute result
     */
    private String execResult;

    /**
     * result status: -1: deleted, 0: normal
     */
    private Integer status;

    /**
     * owner of the task
     */
    private Long userId;

    /**
     * task execution batch id
     */
    private String batchId;
}
