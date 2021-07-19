package com.hokage.biz.response.home;

import lombok.Data;

import java.util.Map;

/**
 * @author yiminlin
 * @date 2021/07/19 9:15 pm
 * @description home detail meta
 **/
@Data
public class HomeDetailMeta {
    private Integer total;
    Map<String, Integer> groupInfo;
}
