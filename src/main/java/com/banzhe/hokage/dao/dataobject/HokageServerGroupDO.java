package com.banzhe.hokage.dao.dataobject;

import lombok.Data;

/**
 * @author linyimin
 * @date 2020/7/26 9:51 下午
 * @email linyimin520812@gmail.com
 * @description 服务器分组配置信息表
 */

@Data
public class HokageServerGroupDO {
    private Long id;            // 服务器组id
    private String name;        // 服务器组名称
    private String description; // 服务器组描述
}
