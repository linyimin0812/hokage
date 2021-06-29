package com.hokage.biz.response.bat;

import lombok.Data;

/**
 * @author yiminlin
 * @date 2021/06/28 12:21 am
 * @description task result detail VO
 **/
@Data
public class TaskResultDetailVO {
    private Long id;
    private String execServer;
    private String startTime;
    private String endTime;
    private Long cost;
    private Integer status;
    private Integer taskStatus;
    private Integer exitCode;
    private String execResult;
}
