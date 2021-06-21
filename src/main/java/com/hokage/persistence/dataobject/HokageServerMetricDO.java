package com.hokage.persistence.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yiminlin
 * @date 2021/06/21 11:38 pm
 * @description hokage system metric DO
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class HokageServerMetricDO extends HokageBaseDO {
    private Long id;
    private Integer type;
    private String name;
    private Double value;
    private Long timestamp;
}
