package com.hokage.biz.response.server;

import lombok.Data;

/**
 * @author yiminlin
 * @date 2021/07/20 11:37 pm
 * @description server account vo
 **/
@Data
public class ServerAccountVO {
    /**
     * user id
     */
    private Long id;
    private String username;
    private String account;
    private String createdTime;
    private String latestLoginTime;
}
