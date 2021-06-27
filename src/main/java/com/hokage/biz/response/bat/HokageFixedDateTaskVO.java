package com.hokage.biz.response.bat;

import lombok.Data;

import java.util.List;

/**
 * @author yiminlin
 * @date 2021/06/27 7:10 am
 * @description fixed date task VO
 **/
@Data
public class HokageFixedDateTaskVO {
    /**
     * task id
     */
    private Long id;

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

    private List<String> execServerList;
    /**
     * command
     */
    private String execCommand;

    private Integer status;
}
