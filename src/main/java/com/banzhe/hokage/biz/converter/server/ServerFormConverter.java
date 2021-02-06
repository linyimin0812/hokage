package com.banzhe.hokage.biz.converter.server;

import com.banzhe.hokage.biz.converter.Converter;
import com.banzhe.hokage.biz.form.server.HokageServerForm;
import com.banzhe.hokage.persistence.dataobject.HokageServerDO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * @author linyimin
 * @date 2021/2/6 20:46
 * @email linyimin520812@gmail.com
 * @description server form converter
 */
@Component
public class ServerFormConverter implements Converter<HokageServerForm, HokageServerDO> {

    @Override
    public HokageServerDO doForward(HokageServerForm hokageServerForm) {
        HokageServerDO serverDO = new HokageServerDO();
        BeanUtils.copyProperties(hokageServerForm, serverDO);
        return serverDO;
    }

    @Override
    public HokageServerForm doBackward(HokageServerDO serverDO) {
        HokageServerForm form = new HokageServerForm();
        BeanUtils.copyProperties(serverDO, form);
        return form;
    }
}
