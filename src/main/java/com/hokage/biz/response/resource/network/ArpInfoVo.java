package com.hokage.biz.response.resource.network;

import lombok.Data;

/**
 * @author yiminlin
 * @date 2021/06/21 1:14 上午
 * @description arp information VO
 **/
@Data
public class ArpInfoVo {
    private String ip;
    private String hwType;
    private String mac;
    private String interfaceName;
}
