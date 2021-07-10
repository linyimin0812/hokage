package com.hokage.persistence.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author linyimin
 * @date 2020/7/26 10:07 pm
 * @email linyimin520812@gmail.com
 * @description server security group
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class HokageSecurityGroupDO extends HokageBaseDO {
    /**
     * security group id
     */
    private Long id;
    /**
     * user primary id
     */
    private Long userId;
    /**
     * acts on the server group
     */
    private String servers;
    /**
     * auth strategy
     */
    private Integer authStrategy;
    /**
     * protocol type
     */
    private Integer protocolType;
    /**
     * port range
     */
    private String portRange;
    /**
     * auth object
     */
    private String authObject;
    /**
     * status
     */
    private Integer status;
    private String description;
}
