package com.banzhe.hokage.biz.form.server;

import com.banzhe.hokage.biz.enums.ServerLabel;
import com.banzhe.hokage.common.PageQuery;
import lombok.Data;

/**
 * @author linyimin
 * @date 2020/11/1 5:29 下午
 * @email linyimin520812@gmail.com
 * @description
 */
@Data
public class ServerSearchForm extends PageQuery {
    private String hostname;            // 主机名
    private String supervisorName;      // 管理员姓名
    private String label;               // 服务器标签
    private String status;              // 服务器状态
}
