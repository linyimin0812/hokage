package com.hokage.biz.response.resource;

import lombok.Data;

/**
 * @author yiminlin
 * @date 2021/06/17 2:20 am
 * @description general information
 **/
@Data
public class GeneralInfoVO {
    private String name;
    private String value;

    public GeneralInfoVO(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
