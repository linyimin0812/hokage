package com.hokage.biz.form.user;

import com.hokage.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/11/1 16:00
 * @email linyimin520812@gmail.com
 * @description supervisor information search form
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserServerSearchForm extends PageQuery {
    /**
     * operator id
     */
    private Long operatorId;

    /**
     * primary key
     */
    private Long id;
    /**
     * user name
     */
    private String username;
    /**
     * server group
     */
    private List<String> serverGroup;

}
