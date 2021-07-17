package com.hokage.biz.converter.server;

import com.hokage.biz.converter.Converter;
import com.hokage.biz.form.server.ServerOperateForm;
import com.hokage.biz.response.HokageOptionVO;
import com.hokage.persistence.dataobject.HokageServerGroupDO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * @author yiminlin
 * @date 2021/07/17 6:36 pm
 * @description server group converter
 **/
@Component
public class ServerGroupConverter implements Converter<ServerOperateForm.ServerGroupForm, HokageServerGroupDO> {
    @Override
    public HokageServerGroupDO doForward(ServerOperateForm.ServerGroupForm form) {
        HokageServerGroupDO groupDO = new HokageServerGroupDO();
        BeanUtils.copyProperties(form, groupDO);
        return groupDO;
    }

    @Override
    public ServerOperateForm.ServerGroupForm doBackward(HokageServerGroupDO groupDO) {
        ServerOperateForm.ServerGroupForm form = new ServerOperateForm.ServerGroupForm();
        BeanUtils.copyProperties(groupDO, form);
        return form;
    }

    public HokageOptionVO<String> toOption(HokageServerGroupDO groupDO) {
        String label = String.format("%s(%s)", groupDO.getName(), groupDO.getDescription());
        return new HokageOptionVO<>(label, groupDO.getName());
    }
}
