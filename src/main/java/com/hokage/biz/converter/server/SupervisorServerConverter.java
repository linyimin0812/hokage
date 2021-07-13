package com.hokage.biz.converter.server;

import com.hokage.biz.converter.Converter;
import com.hokage.biz.form.user.UserServerOperateForm;
import com.hokage.biz.request.server.SupervisorServerQuery;
import org.springframework.stereotype.Component;

/**
 * @author yiminlin
 * @date 2021/07/13 9:43 pm
 * @description supervisor server converter
 **/
@Component
public class SupervisorServerConverter implements Converter<UserServerOperateForm, SupervisorServerQuery> {
    @Override
    public SupervisorServerQuery doForward(UserServerOperateForm form) {
        SupervisorServerQuery query = new SupervisorServerQuery();
        query.setSupervisorId(form.getUserIds().get(0));
        return query;
    }

    @Override
    public UserServerOperateForm doBackward(SupervisorServerQuery query) {
        return null;
    }
}
