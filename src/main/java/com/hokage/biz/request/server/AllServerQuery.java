package com.hokage.biz.request.server;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author linyimin
 * @date 2020/12/20 23:04
 * @email linyimin520812@gmail.com
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AllServerQuery extends ServerQuery {

    /**
     * supervisor
     */
    private String supervisor;

}
