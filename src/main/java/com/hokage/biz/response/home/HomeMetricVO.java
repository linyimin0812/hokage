package com.hokage.biz.response.home;

import com.hokage.biz.response.resource.metric.MetricVO;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author yiminlin
 * @date 2021/07/20 2:18 am
 * @description home metric vo
 **/
@Data
@AllArgsConstructor
public class HomeMetricVO {
    private String serverIp;
    private MetricVO metricVO;
}
