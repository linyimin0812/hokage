package com.hokage.persistence.dataobject;

import lombok.Data;

import java.util.Date;

/**
 * @author linyimin
 * @date 2020/8/22 4:31 pm
 * @email linyimin520812@gmail.com
 * @description data object's base property
 */
@Data
public class HokageBaseDO {
    /**
     * create time
     */
    private Date gmtCreate;
    /**
     * modified time
     */
    private Date gmtModified;

    /**
     * record status, see {@link com.hokage.biz.enums.RecordStatusEnum}
     */
    private Integer status;
}
