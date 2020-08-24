package com.banzhe.hokage.biz.dto;

import lombok.Data;

/**
 * @author linyimin
 * @date 2020/7/26 10:21 下午
 * @email linyimin520812@gmail.com
 * @description 普通用户和服务器颖映射表
 */
@Data
public class HokageSubordinateServerDTO {
    private Long id;
    private Long subordinateId; // 普通用户id
    private Long serverId;      // 服务器id
}
