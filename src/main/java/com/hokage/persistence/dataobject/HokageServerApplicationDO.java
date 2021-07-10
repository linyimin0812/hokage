package com.hokage.persistence.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author linyimin
 * @date 2021/2/16 15:23 pm
 * @email linyimin520812@gmail.com
 * @description
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class HokageServerApplicationDO extends HokageBaseDO {
    /**
     * apply id
     */
    private Long id;

    /**
     * server id
     */
    private Long serverId;

    /**
     * apply id
     */
    private Long applyId;

    /**
     * approve id
     */
    private String approveIds;

    /**
     * actual approve id
     */
    private Long actualApproveId;
}
