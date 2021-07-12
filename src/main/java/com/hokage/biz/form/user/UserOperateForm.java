package com.hokage.biz.form.user;

import lombok.Data;

import java.util.List;

/**
 * @author yiminlin
 * @date 2021/07/12 11:39
 * @description user operate form
 **/
@Data
public class UserOperateForm {
    /**
     * operator id
     */
    private Long operatorId;

    private Long supervisorId;

    /**
     * user ids
     */
    private List<Long> userIds;
}
