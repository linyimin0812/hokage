package com.hokage.biz.response.resource.metric;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author yiminlin
 * @date 2021/06/23 1:55 am
 * @description metric meta
 **/
@Data
@Accessors(chain = true)
public class MetricMetaVO {
    private List<String> timeList;
    private List<SeriesVO> series;

    @Data
    @Accessors(chain = true)
    public static class SeriesVO {
        private String name;
        private List<Double> data;
    }
}
