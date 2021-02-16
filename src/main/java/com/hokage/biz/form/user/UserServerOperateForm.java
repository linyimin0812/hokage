package com.hokage.biz.form.user;

import lombok.Data;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/11/1 5:10 pm
 * @email linyimin520812@gmail.com
 * @description user operation form
 */
@Data
public class UserServerOperateForm {
    /**
     * operator id
     */
    private Long id;
    /**
     * server ids
     */
    private List<Long> serverIds;
    /**
     * user ids
     */
    private List<Long> userIds;
}
