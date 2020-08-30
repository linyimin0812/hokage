package com.banzhe.hokage.biz.converter;

import com.banzhe.hokage.biz.form.user.HokageUserLoginForm;
import com.banzhe.hokage.biz.form.user.HokageUserRegisterForm;
import com.banzhe.hokage.persistence.dataobject.HokageUserDO;
import org.springframework.beans.BeanUtils;


/**
 * @author linyimin
 * @date 2020/8/30 3:14 下午
 * @email linyimin520812@gmail.com
 * @description 用户VO与DO之间的转换
 */
public class UserConverter {
    public static HokageUserDO registerFormToDO(HokageUserRegisterForm userRegisterForm) {
        HokageUserDO userDO = new HokageUserDO();
        BeanUtils.copyProperties(userRegisterForm, userDO);
        return userDO;
    }
    public static HokageUserRegisterForm DOToRegisterForm(HokageUserDO userDO) {
        HokageUserRegisterForm form = new HokageUserRegisterForm();
        BeanUtils.copyProperties(userDO, form);
        return form;
    }
}
