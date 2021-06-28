package com.hokage.biz.response.bat;

import lombok.Data;

import java.util.List;

/**
 * @author yiminlin
 * @date 2021/06/28 12:20 am
 * @description task result VO
 **/
@Data
public class TaskResultVO {
    private Long taskId;
    private String taskName;
    private Integer taskStatus;
    private Integer triggerType;
    private String startTime;
    private String endTime;
    private Long cost;
    private Integer exitCode;
    private String batchId;
    private List<TaskResultDetailVO> resultDetailVOList;
}
