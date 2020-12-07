package com.banzhe.hokage.persistence.dataobject;

import lombok.Data;

import java.util.Date;

/**
 * @author linyimin
 * @date 2020/7/26 9:40 pm
 * @email linyimin520812@gmail.com
 */

@Data
public class HokageUserDO extends HokageBaseDO {
    /**
     * user id
     */
    private Long id;
    /**
     * username
     */
    private String username;
    /**
     * user password
     */
    private String passwd;
    /**
     * role of user: 100: super , 1: supervisor, 2: ordinary
     */
    private Integer role;
    /**
     * user email
     */
    private String email;
    /**
     * subscribe or not: 0: dont subscribe, 1: subscribe, when any change, will send user a eamil
     */
    private Integer subscribed;
}