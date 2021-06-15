package com.hokage.biz.response.resource;

import lombok.Data;

import java.util.Map;

/**
 * @author yiminlin
 * @date 2021/06/16 12:48 上午
 * @description server base information
 **/
@Data
public class BasicInfoVO {
    private Map<String, String> cpuInfo;
    private Map<String, String> memInfo;
}
