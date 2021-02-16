package com.hokage.biz.request;

import lombok.Data;

/**
 * @author linyimin
 * @date 2020/12/23 22:48
 * @email linyimin520812@gmail.com
 * @description
 */
@Data
public class SupervisorServerQuery extends ServerQuery {
    /**
     * ssh account
     */
    private String account;

    /**
     * username
     */
    private String username;
}
