package com.hokage.biz.form.monitor;

import lombok.Data;

/**
 * @author yiminlin
 * @date 2021/06/16 9:18 上午
 * @description server monitor operate form
 **/
@Data
public class MonitorOperateForm {
    private Long operatorId;
    private String ip;
    private String sshPort;
    private String account;
}
