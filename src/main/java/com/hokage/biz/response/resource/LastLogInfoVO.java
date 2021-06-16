package com.hokage.biz.response.resource;

import lombok.Data;

/**
 * @author yiminlin
 * @date 2021/06/17 2:22 am
 * @description last login information
 **/
@Data
public class LastLogInfoVO {
    private String account;
    private String port;
    private String from;
    private String latest;
}
