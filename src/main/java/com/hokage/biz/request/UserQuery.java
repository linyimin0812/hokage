package com.hokage.biz.request;

import com.hokage.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author linyimin
 * @date 2021/03/27 14:10
 * @email linyimin520812@gmail.com
 * @description user query
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserQuery extends PageQuery {
    /**
     * primary key
     */
    private Long id;

    /**
     * username
     */
    private String username;

    /**
     * server group
     */
    private String serverGroup;

    /**
     * user role
     */
    private Integer role;
}
