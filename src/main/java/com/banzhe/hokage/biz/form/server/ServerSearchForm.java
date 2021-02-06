package com.banzhe.hokage.biz.form.server;

import com.banzhe.hokage.biz.enums.ServerLabel;
import com.banzhe.hokage.common.PageQuery;
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
    private Long operateId;
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
    private String ipAddress;

    /**
     * supervisor name
     */
    private String supervisorName;
    /**
     * server label
     */
    private String label;
    /**
     * server status
     */
    private String status;

    /**
     * server group
     */
    private String serverGroup;

    /**
     * server status
     */
    private String serverStatus;

    /**
     * server ssh account
     */
    private String account;

    /**
     * account status
     */
    private String accountStatus;

}

