package com.hokage.biz.form.bat;

import lombok.Data;

import java.util.List;

/**
 * @author yiminlin
 * @date 2021/06/27 4:45 am
 * @description fixed date task form
 **/
@Data
public class HokageFixedDateTaskForm {
    /**
     * task id
     */
    private Long id;
    /**
     * user id to which the task belongs
     */
    private Long operatorId;

    /**
     * task type: 0-shell
     */
    private Integer taskType;

    /**
     * task name
     */
    private String taskName;

    /**
     * execute type: 0-fixed date, 1- cron
     */
    private Integer execType;
    /**
     * execute time
     */
    private String execTime;
    /**
     * server id list or server group id list
     */
    private List<Long> execServers;
    /**
     * command
     */
    private String execCommand;
}
