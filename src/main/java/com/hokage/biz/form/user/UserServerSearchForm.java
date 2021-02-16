package com.hokage.biz.form.user;

import com.hokage.common.PageQuery;
import lombok.Data;

/**
 * @author linyimin
 * @date 2020/11/1 16:00
 * @email linyimin520812@gmail.com
 * @description supervisor information search form
 */
@Data
public class UserServerSearchForm extends PageQuery {
    /**
     * user id
     */
    private Long id;
    /**
     * user name
     */
    private String username;
    /**
     * server label
     */
    private String label;

}
