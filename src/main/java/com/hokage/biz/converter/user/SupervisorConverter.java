package com.hokage.biz.converter.user;

import com.hokage.biz.converter.Converter;
import com.hokage.biz.form.user.UserSearchForm;
import com.hokage.biz.request.user.SupervisorQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * @author yiminlin
 * @date 2021/07/12 7:25 pm
 * @description supervisor query converter
 **/
@Component
public class SupervisorConverter implements Converter<UserSearchForm, SupervisorQuery> {

    @Override
    public SupervisorQuery doForward(UserSearchForm form) {
        SupervisorQuery query = new SupervisorQuery();
        BeanUtils.copyProperties(form, query);
        query.setSupervisorName(form.getUsername());
        return query;
    }

    @Override
    public UserSearchForm doBackward(SupervisorQuery query) {
        UserSearchForm form = new UserSearchForm();
        BeanUtils.copyProperties(query, form);
        form.setUsername(query.getSupervisorName());
        return form;
    }
}
