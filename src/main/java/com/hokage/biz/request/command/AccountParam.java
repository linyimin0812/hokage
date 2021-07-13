package com.hokage.biz.request.command;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author yiminlin
 * @date 2021/07/13 11:28 am
 * @description account param
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class AccountParam extends BaseCommandParam{
    private String account;
    private String passwd;
}
