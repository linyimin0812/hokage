package com.hokage.persistence.dataobject;

import lombok.Data;

/**
 * @author linyimin
 * @date 2020/7/26 10:23 下午
 * @email linyimin520812@gmail.com
 * @description 管理员和服务器颖映射表
 */
@Data
public class HokageSupervisorServerDO extends HokageBaseDO {
    private Long id;
    private Long supervisorId;  // 管理员id
    private Long serverId;      // 服务器id
}
