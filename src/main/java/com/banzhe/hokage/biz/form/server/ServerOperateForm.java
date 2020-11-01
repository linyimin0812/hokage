package com.banzhe.hokage.biz.form.server;

import lombok.Data;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/11/1 5:38 下午
 * @email linyimin520812@gmail.com
 * @description 服务器操作表单信息
 */
@Data
public class ServerOperateForm {
    private Long id;                //   操作者id
    private List<Long> userIds;     // 用户id
    private List<Long> serverIds;   // 服务器id
}
