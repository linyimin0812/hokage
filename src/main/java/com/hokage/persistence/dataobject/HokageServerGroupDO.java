package com.hokage.persistence.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author linyimin
 * @date 2020/7/26 9:51 pm
 * @email linyimin520812@gmail.com
 * @description server group data object
 */


@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class HokageServerGroupDO extends HokageBaseDO {
    /**
     * server group id
     */
    private Long id;
    /**
     * server group name
     */
    private String name;
    /**
     * server group description
     */
    private String description;
    /**
     * server group creator
     */
    private Long creatorId;
}
