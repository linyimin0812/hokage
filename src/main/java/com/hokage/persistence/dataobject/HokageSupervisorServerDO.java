package com.hokage.persistence.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author linyimin
 * @date 2020/7/26 10:23 pm
 * @email linyimin520812@gmail.com
 * @description supervisor and server relationship
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HokageSupervisorServerDO extends HokageBaseDO {
    private Long id;
    /**
     * supervisor id
     */
    private Long supervisorId;
    /**
     * server id
     */
    private Long serverId;

    /**
     * status, {@link com.hokage.biz.enums.RecordStatusEnum}
     */
    private Integer status;
}
