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
     * status, {@link com.hokage.biz.enums.RecordStatusEnum}
     */
    private Integer status;
}
