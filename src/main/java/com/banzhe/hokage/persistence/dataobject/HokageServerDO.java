package com.banzhe.hokage.persistence.dataobject;

import lombok.Data;

/**
 * @author linyimin
 * @date 2020/7/26 10:12 pm
 * @email linyimin520812@gmail.com
 * @description server information
 */

@Data
public class HokageServerDO extends HokageBaseDO {
    /**
     * server id
     */
    private Long id;
    /**
     * server hostname
     */
    private String hostname;
    /**
     * server domain
     */
    private String domain;
    /**
     * server ip
     */
    private String ip;
    /**
     * server ssh port
     */
    private String sshPort;
    /**
     * server ssh account
     */
    private String account;
    /**
     * server ssh password
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
     * server description
     */
    private String description;

    /**
     * creator
     */
    private String creator;
}
