package com.banzhe.hokage.persistence.dataobject;

import lombok.Data;

/**
 * @author linyimin
 * @date 2020/8/30 4:28 下午
 * @email linyimin520812@gmail.com
 * @description
 */
@Data
public class HokageSequenceDO extends HokageBaseDO {
    private Long id;        // 序列id
    private String name;    // 序列名称
    private Long value;     // 序列值
}
