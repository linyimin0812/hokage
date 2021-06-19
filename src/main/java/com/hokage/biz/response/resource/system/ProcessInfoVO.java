package com.hokage.biz.response.resource.system;

import lombok.Data;

/**
 * @author yiminlin
 * @date 2021/06/19 3:59 pm
 * @description process info VO
 **/
@Data
public class ProcessInfoVO {
    private Long pid;
    private String account;
    private Float cpu;
    private Float mem;
    private Long rss;
    private Long vsz;
    private String started;
    private String status;
    private String comm;
    private String command;
}
