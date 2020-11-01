package com.banzhe.hokage.biz.form.user;

import lombok.Data;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/11/1 5:10 下午
 * @email linyimin520812@gmail.com
 * @description 用户操作信息表单
 */
@Data
public class UserServerOperateForm {
    private Long id;                // 操作者id
    private List<Long> serverIds;   // 服务器id
    private List<Long> userIds;     // 用户id
}
