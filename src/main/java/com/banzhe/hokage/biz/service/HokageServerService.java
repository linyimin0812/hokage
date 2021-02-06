package com.banzhe.hokage.biz.service;

import com.banzhe.hokage.biz.form.server.ServerSearchForm;
import com.banzhe.hokage.biz.request.AllServerQuery;
import com.banzhe.hokage.biz.request.SubordinateServerQuery;
import com.banzhe.hokage.biz.request.SupervisorServerQuery;
import com.banzhe.hokage.biz.response.server.HokageServerVO;
import com.banzhe.hokage.common.ServiceResponse;
import com.banzhe.hokage.persistence.dataobject.HokageServerDO;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/8/22 11:28 am
 * @email linyimin520812@gmail.com
 * @description server service interface
 */
public interface HokageServerService {
    /**
     * insert a new record
     * @param serverDO
     * @return
     */
    ServiceResponse<Long> insert(HokageServerDO serverDO);

    /**
     * update a record
     * @param serverDO
     * @return
     */
    ServiceResponse<Long> update(HokageServerDO serverDO);

    /**
     * retrieve all server
     * @return
     */
    ServiceResponse<List<HokageServerVO>> selectAll();

    /**
     * search server
     * @param form ServerSearchForm
     * @return List<HokageServerVO>
     */
    ServiceResponse<List<HokageServerVO>> listServer(ServerSearchForm form);

    /**
     * retrieve server information based on server ids
     * @param ids
     * @return
     */
    ServiceResponse<List<HokageServerDO>> selectByIds(List<Long> ids);


    /**
     * retrieve server information based on server type
     * @param type
     * @return
     */
    ServiceResponse<List<HokageServerDO>> selectByType(String type);

    /**
     * retrieve server information based on server group
     * @param group
     * @return
     */
    ServiceResponse<List<HokageServerDO>> selectByGroup(String group);
}
