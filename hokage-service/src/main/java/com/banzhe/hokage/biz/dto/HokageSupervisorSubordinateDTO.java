package com.banzhe.hokage.biz.dto;

import lombok.Data;

/**
 * @author linyimin
 * @date 2020/7/26 10:18 下午
 * @email linyimin520812@gmail.com
 * @description 管理员与用户关系映射表
 */

@Data
public class HokageSupervisorSubordinateDTO {
    private Long id;
    private Long supervisorId;  // 管理员id
    private Long subordinateId; // 普通用户id
}
