package com.hokage.biz.converter.user;

import com.hokage.biz.converter.Converter;
import com.hokage.biz.form.user.UserServerSearchForm;
import com.hokage.biz.request.UserQuery;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * @author linyimin
 * @date 2020/12/23 22:50
 * @email linyimin520812@gmail.com
 * @description server search form converter
 */
@Component
public class SearchConverter implements Converter<UserServerSearchForm, UserQuery> {
    @Override
    public UserQuery doForward(UserServerSearchForm userServerSearchForm) {
        UserQuery query = new UserQuery();
        if (Objects.nonNull(userServerSearchForm.getId())) {
            query.setId(userServerSearchForm.getId());
        }

        if (!CollectionUtils.isEmpty(userServerSearchForm.getServerGroup())) {
            query.setServerGroup(String.join(",", userServerSearchForm.getServerGroup()));
        }

        if (!StringUtils.isEmpty(userServerSearchForm.getUsername())) {
            query.setUsername(userServerSearchForm.getUsername());
        }

        if (Objects.nonNull(userServerSearchForm.getOperatorId())) {
            query.setOperatorId(userServerSearchForm.getOperatorId());
        }

        return query;
    }

    @Override
    public UserServerSearchForm doBackward(UserQuery query) {
        return null;
    }
}
