package com.hokage.biz.response.resource.system;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author yiminlin
 * @date 2021/06/20 6:11 pm
 * @description disk info VO
 **/
@Data
@Accessors(chain = true)
public class DiskInfoVO {
    private String name;
    private String size;
    private String used;
    private Integer capacity;
    private String mounted;
}
