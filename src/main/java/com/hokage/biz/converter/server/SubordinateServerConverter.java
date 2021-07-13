package com.hokage.biz.converter.server;

import com.hokage.biz.converter.Converter;
import com.hokage.biz.form.server.ServerSearchForm;
import com.hokage.biz.request.server.SubordinateServerQuery;
import org.springframework.stereotype.Component;

/**
 * @author yiminlin
 * @date 2021/07/13 5:47 pm
 * @description subordinate server converter
 **/
@Component
public class SubordinateServerConverter implements Converter<ServerSearchForm, SubordinateServerQuery> {
    @Override
    public SubordinateServerQuery doForward(ServerSearchForm serverSearchForm) {
        SubordinateServerQuery query = new SubordinateServerQuery();
        query.setSupervisorId(serverSearchForm.getOperatorId());
        query.setSubordinateId(serverSearchForm.getUserId());
        return query;
    }

    @Override
    public ServerSearchForm doBackward(SubordinateServerQuery subordinateServerQuery) {
        return null;
    }
}
