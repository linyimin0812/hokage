package com.banzhe.hokage.biz.response.server;

import com.banzhe.hokage.biz.response.HokageOperation;
import lombok.Data;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/10/28 12:32 上午
 * @email linyimin520812@gmail.com
 * @description 服务器具体信息
 */
@Data
public class HokageServerVO {
    private Long id;                                // 服务器对应id
    private String hostname;                        // 主机名
    private String domain;                          // 域名
    private List<String> labels;                    // 标签
    private String supervisor;                      // 管理员
    private Integer userNum;                        // 用户数量
    private String status;                          // 状态
    private List<HokageOperation> operationList;    //  操作
}
