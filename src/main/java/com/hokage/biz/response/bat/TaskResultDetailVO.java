package com.hokage.biz.response.bat;

import lombok.Data;

/**
 * @author yiminlin
 * @date 2021/06/28 12:21 am
 * @description task result detail VO
 **/
@Data
public class TaskResultDetailVO {
    private String serverIp;
    private String startTime;
    private String endTime;
    private Long cost;
    private Integer status;
}
