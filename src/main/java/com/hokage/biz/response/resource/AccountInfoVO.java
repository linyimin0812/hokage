package com.hokage.biz.response.resource;

import lombok.Data;

/**
 * @author yiminlin
 * @date 2021/06/17 2:21 am
 * @description account information
 **/
@Data
public class AccountInfoVO {
    private String type;
    private String account;
    private String home;
    private String description;
}
