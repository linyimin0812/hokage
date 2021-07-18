package com.hokage.biz.request.server;

import com.hokage.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yiminlin
 * @date 2021/07/13 5:44 pm
 * @description subordinate server query
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class SubordinateServerQuery extends ServerQuery {
    private Long supervisorId;
    private Long subordinateId;
    /**
     * ssh account
     */
    private String account;
    /**
     * username
     */
    private String username;
}
