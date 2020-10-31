package com.banzhe.hokage.biz.response.user;

import com.banzhe.hokage.biz.response.HokageOperation;
import com.banzhe.hokage.biz.response.server.HokageServerVO;
import lombok.Data;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/10/28 12:26 上午
 * @email linyimin520812@gmail.com
 * @description 用户管理
 */
@Data
public class HokageUserVO {
    private Long id;                        // 用户id
    private String username;                // 用户名称
    private String email;                   // 用户邮箱
    private Integer role;                   // 角色
    private Integer serverNum;              // 使用或负责服务器数量
    private List<String> serverLabel;       // 服务器标签
    List<HokageOperation> operationList;    // 用户对应的操作
    List<HokageServerVO> serverVOList;      // 使用或者管理的服务器具体信息
}
