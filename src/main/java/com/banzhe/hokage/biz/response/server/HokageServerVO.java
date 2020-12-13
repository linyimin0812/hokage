package com.banzhe.hokage.biz.response.server;

import com.banzhe.hokage.biz.response.HokageOperation;
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
     * server labels
     */
    private List<String> labels;
    /**
     * supervisor name
     */
    private String supervisor;
    /**
     * supervisor id
     */
    private Long supervisorId;

    /**
     * subordinate name
     */
    private String subordinate;

    /**
     * subordinate id
     */
    private Long subordinateId;

    /**
     * number of user
     */
    private Integer userNum;
    /**
     * status of server
     */
    private String status;
    /**
     * operations
     */
    private List<HokageOperation> operationList;
}
