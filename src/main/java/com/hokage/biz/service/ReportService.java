package com.hokage.biz.service;

import com.hokage.biz.response.resource.network.InterfaceIpVO;

import java.util.List;

/**
 * @author yiminlin
 * @date 2021/07/08 1:04 am
 * @description server report info handler service
 **/
public interface ReportService {
    /**
     * handle report ip info
     * @param interfaceList interface info list
     */
    void ipHandle(List<InterfaceIpVO> interfaceList);
}
