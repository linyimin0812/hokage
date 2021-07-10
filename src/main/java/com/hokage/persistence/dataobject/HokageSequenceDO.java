package com.hokage.persistence.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author linyimin
 * @date 2020/8/30 4:28 pm
 * @email linyimin520812@gmail.com
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HokageSequenceDO extends HokageBaseDO {
    /**
     * sequence id
     */
    private Long id;
    /**
     * sequence name
     */
    private String name;
    /**
     * sequence name
     */
    private Long value;
}
