package com.hokage.ssh.enums;

/**
 * @author yiminlin
 * @date 2021/06/22 1:19 am
 * @description metric type
 **/
public enum MetricTypeEnum {
    /**
     * metric type
     */
    upload(1),
    download(2),

    loadAverage(3),

    memory(4),
    cpu(5)
    ;

    public Integer getValue() {
        return this.value;
    }

    private Integer value;

    MetricTypeEnum(Integer value) {
        this.value = value;
    }
}
