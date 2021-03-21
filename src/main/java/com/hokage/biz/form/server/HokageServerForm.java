package com.hokage.biz.form.server;

import lombok.Data;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/11/1 5:51 pm
 * @email linyimin520812@gmail.com
 * @description server info form
 */
@Data
public class HokageServerForm {

    /**
     * server id, primary key
     */
    private Long id;

    /**
     * domain
     */
    private String domain;
    /**
     * server ip
     */
    private String ip;
    /**
     * ssh port
     */
    private String sshPort;
    /**
     * ssh account, which is used to
     */
    private String account;
    /**
     * passwd
     * TODO: support ssh key
     */
    private String passwd;
    /**
     * server group
     */
    private List<String> serverGroupList;
    /**
     * server operator
     */
    private List<Long> supervisorList;

    /**
     * server description
     */
    private String description;

    /**
     * 操作人id
     */
    private Long operatorId;

    /**
     * login type
     */
    private Integer loginType;
}
