package com.hokage.biz.form.file;

import lombok.Data;

/**
 * @author linyimin
 * @date 2021/05/24 20:59 pm
 * @email linyimin520812@gmail.com
 * @description file operate form
 */
@Data
public class FileOperateForm {
    private Long operatorId;
    private String ip;
    private String sshPort;
    private String account;
    private String curDir;
    private String action;
    private Long page;
    private String dst;
    private String permission;

    public String buildKey() {
        return String.format("%s_%s_%s", ip, sshPort, account);
    }
}
