package com.hokage.persistence.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author linyimin
 * @date 2020/7/26 9:59 下午
 * @email linyimin520812@gmail.com
 * @description 批量任务执行结果表
 */

@Data
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
     * execute server id
     */
    private Long execServer;
    /**
     * execute result
     */
    private String execResult;

    private Integer status;
}
