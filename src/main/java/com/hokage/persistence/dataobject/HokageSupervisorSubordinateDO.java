package com.hokage.persistence.dataobject;

import lombok.Data;

/**
 * @author linyimin
 * @date 2020/7/26 10:18 下午
 * @email linyimin520812@gmail.com
 * @description 管理员与用户关系映射表
 */

@Data
public class HokageSupervisorSubordinateDO extends HokageBaseDO {
    private Long id;
    /**
     * supervisor id
     */
    private Long supervisorId;
    /**
     * subordinate id
     */
    private Long subordinateId;
    /**
     * status, {@link com.hokage.biz.enums.RecordStatusEnum}
     */
    private Integer status;
}
