package com.hokage.biz.service;

import com.hokage.biz.response.home.HomeDetailVO;
import com.hokage.common.ServiceResponse;
import com.hokage.persistence.dataobject.HokageServerDO;

/**
 * @author yiminlin
 * @date 2021/07/19 9:21 pm
 * @description home service
 **/
public interface HomeService {
    /**
     * acquire home detail information
     * @return {@link HomeDetailVO}
     */
    ServiceResponse<HomeDetailVO> homeDetail();

    /**
     * acquire most busy server (CPU utilization max)
     * @return server ip
     */
    ServiceResponse<String> acquireMostBusyServerIp();
}
