package com.hokage.biz.form.monitor;

import lombok.Data;

/**
 * @author yiminlin
 * @date 2021/06/16 9:18 am
 * @description server monitor operate form
 **/
@Data
public class MonitorOperateForm {
    private Long operatorId;
    private Long serverId;
    private String ip;
    private String sshPort;
    private String account;

    private Long pid;
    private Long start;
    private Long end;

    public String buildKey() {
        return String.format("%s_%s_%s_%s", serverId, ip, sshPort, account);
    }
}
