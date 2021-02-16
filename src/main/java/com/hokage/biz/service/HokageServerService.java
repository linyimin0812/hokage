package com.hokage.biz.service;

import com.hokage.biz.form.server.HokageServerForm;
import com.hokage.biz.form.server.ServerOperateForm;
import com.hokage.biz.form.server.ServerSearchForm;
import com.hokage.biz.response.server.HokageServerVO;
import com.hokage.common.ServiceResponse;
import com.hokage.persistence.dataobject.HokageServerDO;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/8/22 11:28 am
 * @email linyimin520812@gmail.com
 * @description server service interface
 */
public interface HokageServerService {

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

    /**
     * insert or update server info
     * @param form
     * @return
     */
    ServiceResponse<HokageServerForm> save(HokageServerForm form);

    /**
     * delete server
     * @param form
     * @return
     */
    ServiceResponse<Boolean> delete(ServerOperateForm form);

    /**
     * designate servers to a supervisor
     * @param form
     * @return
     */
    ServiceResponse<Boolean> designateSupervisor(ServerOperateForm form);

    /**
     * revoke a supervisor
     * @param form
     * @return
     */
    ServiceResponse<Boolean> revokeSupervisor(ServerOperateForm form);

    /**
     * designate servers to a subordinate
     * @param form
     * @return
     */
    ServiceResponse<Boolean> designateSubordinate(ServerOperateForm form);

    /**
     * revoke a subordinate
     * @param form
     * @return
     */
    ServiceResponse<Boolean> revokeSubordinate(ServerOperateForm form);

    /**
     * apply server
     * @param form
     * @return
     */
    ServiceResponse<Boolean> applyServer(ServerOperateForm form);
}