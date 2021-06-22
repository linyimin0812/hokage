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
    upload(1, "uploadStatMetric"),
    download(2, "downloadStatMetric"),

    loadAverage(3, "loadAvgMetric"),

    memory(4, "memStatMetric"),
    cpu(5, "cpuStatMetric")
    ;

    private Integer value;
    private String field;

    MetricTypeEnum(Integer value, String field) {
        this.value = value;
        this.field = field;
    }

    public Integer getValue() {
        return this.value;
    }
    public String getField() { return this.field; }


}
