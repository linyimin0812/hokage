package com.hokage.biz.response.server;

import com.hokage.biz.response.HokageOperation;
import lombok.Data;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/10/28 12:32 am
 * @email linyimin520812@gmail.com
 * @description detail of server
 */
@Data
public class HokageServerVO {
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
     * server ip address
     */
    private String ip;

    /**
     * login account
     */
    private String account;

    /**
     * server login type: 0 - account and password, 1 - key
     */
    private Integer loginType;

    /**
     * ssh port
     */
    private String sshPort;

    /**
     * server group
     */
    private List<String> serverGroupList;

    /**
     * server group
     */
    private List<String> serverGroupIdList;

    /**
     * supervisor name
     */
    private List<String> supervisorList;
    /**
     * supervisor id
     */
    private List<Long> supervisorIdList;

    /**
     * subordinate name
     */
    private List<String> subordinateName;

    /**
     * subordinate id
     */
    private List<Long> subordinateIdList;

    /**
     * number of user
     */
    private Integer userNum;
    /**
     * status of server
     */
    private Integer status;
    /**
     * operations
     */
    private List<HokageOperation> operationList;

    /**
     * server description
     */
    private String description;
}
