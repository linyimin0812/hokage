package com.banzhe.hokage.controller;

import com.banzhe.hokage.common.BaseController;
import com.banzhe.hokage.persistence.dataobject.HokageUserDO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author linyimin
 * @date 2020/8/23 1:32 上午
 * @email linyimin520812@gmail.com
 * @description 处理用户对应的controller
 */
@RestController
public class UserController extends BaseController {


    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    public String register() {
        HokageUserDO userDO = new HokageUserDO();
        userDO.setUsername("banzhe");
        userDO.setEmail("banzhe@gmail.com");
        userDO.setPasswd("test");
        userDO.setRole(1);
        userDO.setIsSubscribed(1);
        return "Hello World";
    }

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public String login() {
        return "Hello World";
    }
}
