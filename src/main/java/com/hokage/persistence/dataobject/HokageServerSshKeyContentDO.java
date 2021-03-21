package com.hokage.persistence.dataobject;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @date 2021-03-21 20:15
 * @author yiminlin
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HokageServerSshKeyContentDO extends HokageBaseDO {
    /**
     * primary key
     */
    private Long id;
    /**
     * uid
     */
    private String uid;

    /**
     * ssh key file content
     */
    private String content;
}
