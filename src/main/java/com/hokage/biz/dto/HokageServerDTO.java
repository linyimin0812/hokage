package com.hokage.biz.dto;

import lombok.Data;

/**
 * @author linyimin
 * @date 2020/7/26 10:12 pm
 * @email linyimin520812@gmail.com
 * @description server info
 */

@Data
public class HokageServerDTO {
    /**
     * server id
     */
    private Long id;
    /**
     * hostname
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
     * server account
     */
    private String account;

    /**
     * login type: 0-password, 1-key file
     */
    private Integer loginType;

    /**
     * ssh password
     */
    private String passwd;
    /**
     * server group
     */
    private String serverGroup;
    /**
     * server description
     */
    private String description;
}
