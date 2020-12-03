package com.banzhe.hokage.biz.form.user;

import com.banzhe.hokage.common.PageQuery;
import lombok.Data;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/11/1 4:00 下午
 * @email linyimin520812@gmail.com
 * @description 管理员信息查询
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
