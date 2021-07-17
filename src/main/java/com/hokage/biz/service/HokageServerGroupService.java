package com.hokage.biz.service;

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
     * @param serverGroupDO server group data object
     * @return insert success return true, otherwise false
     */
    ServiceResponse<Boolean> insert(HokageServerGroupDO serverGroupDO);

    /**
     * list all server group
     * @return {@link List<HokageOptionVO>>}
     */
    ServiceResponse<List<HokageOptionVO<String>>> listGroupOptions();

    /**
     * update server group
     * @param serverGroupDO server group data object
     * @return update success return true, otherwise false
     */
    ServiceResponse<Boolean> update(HokageServerGroupDO serverGroupDO);

    /**
     * update or insert a record
     * @param serverGroupDO server group data object
     * @return  {@link HokageServerGroupDO}
     */
    ServiceResponse<HokageServerGroupDO> upsert(HokageServerGroupDO serverGroupDO);

    /**
     * list server grpup based-on creator id
     * @param id creator id
     * @return {@link List<HokageServerGroupDO>}
     */
    ServiceResponse<List<HokageServerGroupDO>> listByCreatorId(Long id);

    /**
     * list server group based-on creator id
     * @param id creator id
     * @return {@link List<HokageOptionVO>}
     */
    ServiceResponse<List<HokageOptionVO<String>>> listGroup(Long id);

    /**
     * add server group
     * @param groupDO server group data object
     * @return if add success return true, otherwise false
     */
    ServiceResponse<Boolean> addGroup(HokageServerGroupDO groupDO);
}
