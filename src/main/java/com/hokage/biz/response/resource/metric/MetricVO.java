package com.hokage.biz.response.resource.metric;

import lombok.Data;

/**
 * @author yiminlin
 * @date 2021/06/23 1:58 am
 * @description metric vo
 **/
@Data
public class MetricVO {
    private MetricMetaVO loadAvgMetric;
    private MetricMetaVO cpuStatMetric;
    private MetricMetaVO memStatMetric;

    private MetricMetaVO uploadStatMetric;
    private MetricMetaVO downloadStatMetric;
}
