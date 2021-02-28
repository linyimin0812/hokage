package com.hokage.biz.request;

import com.hokage.common.PageQuery;
import lombok.Data;

/**
 * @author linyimin
 * @date 2020/12/20 23:07
 * @email linyimin520812@gmail.com
 * @description
 */
@Data
public class ServerQuery extends PageQuery {
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
    private String ip;

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
}
