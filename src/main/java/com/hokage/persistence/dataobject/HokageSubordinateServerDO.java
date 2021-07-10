package com.hokage.persistence.dataobject;

import lombok.Data;

/**
 * @author linyimin
 * @date 2020/7/26 10:21 pm
 * @email linyimin520812@gmail.com
 * @description subordinate user and server mapping table
 */
@Data
public class HokageSubordinateServerDO extends HokageBaseDO {
    private Long id;
    /**
     * subordinate id
     */
    private Long subordinateId;
    /**
     * server id
     */
    private Long serverId;

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
     * login type: 0-password, 1-key file
     */
    private Integer loginType;
}
