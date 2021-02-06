package com.banzhe.hokage.biz.converter;

import com.banzhe.hokage.biz.form.server.ServerSearchForm;
import com.banzhe.hokage.biz.request.AllServerQuery;
import com.banzhe.hokage.biz.request.SubordinateServerQuery;
import com.banzhe.hokage.biz.request.SupervisorServerQuery;

/**
 * @author linyimin
 * @date 2020/12/23 22:50
 * @email linyimin520812@gmail.com
 * @description
 */
public class ServerFormConverter {
    public static AllServerQuery converterToAll(ServerSearchForm form) {
        return new AllServerQuery();
    }

    public static SupervisorServerQuery converterToSupervisor(ServerSearchForm form) {
        return new SupervisorServerQuery();
    }

    public static SubordinateServerQuery converterToSubordinate(ServerSearchForm form) {
        return new SubordinateServerQuery();
    }

}
