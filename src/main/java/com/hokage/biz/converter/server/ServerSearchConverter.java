package com.hokage.biz.converter.server;

import com.hokage.biz.form.server.ServerSearchForm;
import com.hokage.biz.request.AllServerQuery;
import com.hokage.biz.request.SubordinateServerQuery;
import com.hokage.biz.request.SupervisorServerQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

/**
 * @author linyimin
 * @date 2020/12/23 22:50
 * @email linyimin520812@gmail.com
 * @description server search form converter
 */
public class ServerSearchConverter {
    public static AllServerQuery converterToAll(ServerSearchForm form) {
        AllServerQuery query = new AllServerQuery();
        BeanUtils.copyProperties(form, query);
        if (!CollectionUtils.isEmpty(form.getServerGroup())) {
            query.setServerGroup(String.join(",", form.getServerGroup()));
        }
        return query;
    }

    public static SupervisorServerQuery converterToSupervisor(ServerSearchForm form) {
        SupervisorServerQuery query = new SupervisorServerQuery();
        BeanUtils.copyProperties(form, query);
        if (!CollectionUtils.isEmpty(form.getServerGroup())) {
            query.setServerGroup(String.join(",", form.getServerGroup()));
        }
        return query;
    }

    public static SubordinateServerQuery converterToSubordinate(ServerSearchForm form) {
        SubordinateServerQuery query =  new SubordinateServerQuery();
        BeanUtils.copyProperties(form, query);
        if (!CollectionUtils.isEmpty(form.getServerGroup())) {
            query.setServerGroup(String.join(",", form.getServerGroup()));
        }
        return query;
    }

}
