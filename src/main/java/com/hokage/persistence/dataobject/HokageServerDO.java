package com.hokage.persistence.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author linyimin
 * @date 2020/7/26 10:12 pm
 * @email linyimin520812@gmail.com
 * @description server information
 */

@Data
@EqualsAndHashCode(callSuper = true)
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
     * login type: 0-password, 1-key file
     */
    private Integer loginType;
    /**
     * server description
     */
    private String description;

    /**
     * creator
     */
    private Long creatorId;

    /**
     * status {@link com.hokage.biz.enums.RecordStatusEnum}
     */
    private Integer status;
}
