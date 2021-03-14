package com.hokage.biz.dto;

import lombok.Data;

/**
 * @author linyimin
 * @date 2020/7/26 10:23 pm
 * @email linyimin520812@gmail.com
 * @description supervisor and server mapping relation
 */
@Data
public class HokageSupervisorServerDTO {
    private Long id;
    /**
     * supervisor id
     */
    private Long supervisorId;
    /**
     * server id
     */
    private Long serverId;
}
