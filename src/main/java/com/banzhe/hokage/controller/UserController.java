package com.banzhe.hokage.controller;

import com.banzhe.hokage.biz.dto.HokageUserDTO;
import com.banzhe.hokage.common.BaseController;
import com.banzhe.hokage.common.ResultVO;
import com.banzhe.hokage.persistence.dataobject.HokageUserDO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author linyimin
 * @date 2020/8/23 1:32 上午
 * @email linyimin520812@gmail.com
 * @description 处理用户对应的controller
 */
@RestController
public class UserController extends BaseController {


    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    public ResultVO<HokageUserDTO> register(@RequestBody @Valid HokageUserDTO userDTO) {
        return success(userDTO);
    }

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public ResultVO<HokageUserDTO> login(@RequestBody @Valid HokageUserDTO userDTO) {
        return success(userDTO);
    }

    @RequestMapping(value = "/user/modify", method = RequestMethod.POST)
    public ResultVO<HokageUserDTO> modify(@RequestBody @Valid HokageUserDTO userDTO) {
        return success(userDTO);
    }
}
