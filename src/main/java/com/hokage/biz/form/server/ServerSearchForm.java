package com.hokage.biz.form.server;

import com.hokage.common.PageQuery;
import lombok.Data;

/**
 * @author linyimin
 * @date 2020/11/1 17:29
 * @email linyimin520812@gmail.com
 * @description server search form
 */
@Data
public class ServerSearchForm extends PageQuery {

    /**
     * operator id
     */
    private Long operatorId;
    /**
     * host name
     */
    private String hostname;

    /**
     * domain
     */
    private String domain;

    /**
     * ip address
     */
    private String ip;

    /**
     * supervisor name
     */
    private String supervisorName;
    /**
     * server label
     */
    private String label;

    /**
     * server group
     */
    private String serverGroup;

    /**
     * server status
     */
    private String status;

    /**
     * server ssh account
     */
    private String account;

    /**
     * account status
     */
    private String accountStatus;

    /**
     * username
     */
    private String username;

}

