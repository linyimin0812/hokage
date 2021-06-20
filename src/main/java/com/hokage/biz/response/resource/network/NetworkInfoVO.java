package com.hokage.biz.response.resource.network;

import lombok.Data;

import java.util.List;

/**
 * @author yiminlin
 * @date 2021/06/21 12:46 上午
 * @description network information
 **/
@Data
public class NetworkInfoVO {
    private List<InterfaceIpVO> interfaceIpInfo;
    private List<ArpInfoVo> arpInfo;
    private List<ConnectionInfoVO> connectionInfo;
}
