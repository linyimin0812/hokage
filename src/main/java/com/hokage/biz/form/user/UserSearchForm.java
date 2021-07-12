package com.hokage.biz.form.user;

import com.hokage.common.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/11/1 16:00 pm
 * @email linyimin520812@gmail.com
 * @description user search form
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserSearchForm extends PageQuery {
    /**
     * operator id
     */
    private Long operatorId;

    /**
     * primary key
     */
    private Long id;
    /**
     * supervisor name
     */
    private String username;
    /**
     * server group
     */
    private List<String> serverGroup;

}
