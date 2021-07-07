package com.hokage.biz.response.resource.metric;

import lombok.Data;

import java.util.List;

/**
 * @author yiminlin
 * @date 2021/06/23 1:58 am
 * @description metric vo
 **/
@Data
public class MetricVO {
    private List<MetricMetaVO> loadAvgMetric;
    private List<MetricMetaVO> cpuStatMetric;
    private List<MetricMetaVO> memStatMetric;

    private List<MetricMetaVO> uploadStatMetric;
    private List<MetricMetaVO> downloadStatMetric;
}
