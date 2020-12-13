package com.banzhe.hokage.persistence.dataobject;

import lombok.Data;

/**
 * @author linyimin
 * @date 2020/7/26 10:21 下午
 * @email linyimin520812@gmail.com
 * @description 普通用户和服务器颖映射表
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
    // TODO: add supervisor id
}
