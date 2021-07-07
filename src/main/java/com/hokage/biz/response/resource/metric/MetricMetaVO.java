package com.hokage.biz.response.resource.metric;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
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
    private String time;
    private Double value;
    private String category;

    public int compareTo(MetricMetaVO that) {
        return ComparisonChain.start()
                .compare(this.category, that.category)
                .compare(this.time, that.time)
                .result();
    }
}
