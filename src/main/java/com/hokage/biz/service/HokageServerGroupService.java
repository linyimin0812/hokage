package com.hokage.biz.service;

import com.hokage.biz.form.server.ServerOperateForm;
import com.hokage.biz.response.HokageOptionVO;
import com.hokage.common.ServiceResponse;
import com.hokage.persistence.dataobject.HokageServerGroupDO;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/8/22 11:14 am
 * @email linyimin520812@gmail.com
 * @description server group service interface
 */
public interface HokageServerGroupService {
    /**
     * insert a new record
     * @param serverGroupDO
     * @return
     */
    ServiceResponse<Boolean> insert(HokageServerGroupDO serverGroupDO);

    /**
     * list all server group
     * @return
     */
    ServiceResponse<List<HokageServerGroupDO>> selectAll();

    /**
     * update server group
     * @param serverGroupDO
     * @return
     */
    ServiceResponse<Boolean> update(HokageServerGroupDO serverGroupDO);

    /**
     * update or insert a record
     * @param serverGroupDO
     * @return
     */
    ServiceResponse<HokageServerGroupDO> upsert(HokageServerGroupDO serverGroupDO);

    /**
     * list server grpup based-on creator id
     * @param id
     * @return
     */
    ServiceResponse<List<HokageServerGroupDO>> listByCreatorId(Long id);

    /**
     * list server group based-on creator id
     * @param id
     * @return
     */
    ServiceResponse<List<HokageOptionVO<Long>>> listGroup(Long id);

    /**
     * add server group
     * @param form
     * @return
     */
    ServiceResponse<Boolean> addGroup(ServerOperateForm form);
}
