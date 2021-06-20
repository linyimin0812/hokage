package com.hokage.biz.response.resource.network;

import lombok.Data;

/**
 * @author yiminlin
 * @date 2021/06/21 1:38 am
 * @description connection information VO
 **/
@Data
public class ConnectionInfoVO {
    private String protocol;
    private String localIp;
    private String foreignIp;
    private String status;
    private String process;
}
