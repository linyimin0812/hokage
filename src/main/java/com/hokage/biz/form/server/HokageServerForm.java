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
    private String serverGroup;

    /**
     * server label: internal, external, X86, GPU
     */
    private String label;
    /**
     * server operator
     */
    private List<Long> supervisors;

    /**
     * server description
     */
    private String description;

}
