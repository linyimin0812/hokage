package com.hokage.biz.request.command;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author yiminlin
 * @date 2021/06/19 7:50 pm
 * @description monitor param
 **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class MonitorParam extends BaseCommandParam {
    private Long pid;
    private String account;
}
