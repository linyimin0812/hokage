package com.hokage.persistence.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Date;

/**
 * @author linyimin
 * @date 2020/7/26 9:53 pm
 * @email linyimin520812@gmail.com
 * @description fixed-date bat command DO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HokageFixedDateTaskDO extends HokageBaseDO {
    /**
     * task id
     */
    private Long id;
    /**
     * user id to which the task belongs
     */
    private Long userId;

    /**
     * task type: 0-shell
     */
    private Integer taskType;

    /**
     * task name
     */
    private String taskName;
    /**
     * execute time
     */
    private Long execTime;
    /**
     * execute server list(server ip or server group)
     */
    private String execServers;
    /**
     * command
     */
    private String execCommand;

}
