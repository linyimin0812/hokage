package com.hokage.biz.service;

import com.hokage.biz.response.resource.metric.MetricVO;
import com.hokage.common.ServiceResponse;

/**
 * @author yiminlin
 * @date 2021/06/23 2:00 am
 * @description server metric service
 **/
public interface HokageServerMetricService {
    /**
     * acquire metric which belong to server and between start and end
     * @param server server, may be ip, or other string which can identify a server
     * @param start start timestamp
     * @param end end timestamp
     * @return metric VO
     */
    ServiceResponse<MetricVO> acquireMetric(String server, Long start, Long end);
}
