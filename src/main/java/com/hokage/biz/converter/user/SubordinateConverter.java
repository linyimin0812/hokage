package com.hokage.biz.converter.user;

import com.hokage.biz.converter.Converter;
import com.hokage.biz.form.user.UserSearchForm;
import com.hokage.biz.request.user.SubordinateQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * @author yiminlin
 * @date 2021/07/12 8:56 pm
 * @description subordinate converter
 **/
@Component
public class SubordinateConverter implements Converter<UserSearchForm, SubordinateQuery> {
    @Override
    public SubordinateQuery doForward(UserSearchForm form) {
        SubordinateQuery query = new SubordinateQuery();
        BeanUtils.copyProperties(form, query);
        query.setSubordinateName(form.getUsername());
        return query;
    }

    @Override
    public UserSearchForm doBackward(SubordinateQuery query) {
        UserSearchForm form = new UserSearchForm();
        BeanUtils.copyProperties(query, form);
        form.setUsername(query.getSubordinateName());
        return form;
    }
}
