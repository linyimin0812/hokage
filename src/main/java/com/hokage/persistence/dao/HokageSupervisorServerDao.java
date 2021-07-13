package com.hokage.persistence.dao;

import com.hokage.biz.response.server.HokageServerVO;
import com.hokage.common.ServiceResponse;
import com.hokage.persistence.dataobject.HokageSupervisorServerDO;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/8/22 12:01 pm
 * @email linyimin520812@gmail.com
 * @description
 */
public interface HokageSupervisorServerDao {
    /**
     * insert a new record
     * @param supervisorServerDO supervisor server data object
     * @return rows affected
     */
    Long insert(HokageSupervisorServerDO supervisorServerDO);

    /**
     * update a recode
     * @param supervisorServerDO supervisor server data object
     * @return rows affected
     */
    Long update(HokageSupervisorServerDO supervisorServerDO);

    /**
     * retrieve supervisor id by server ids
     * @param ids server id list
     * @return supervisor server list which meet the criteria
     */
    List<HokageSupervisorServerDO> listByServerIds(List<Long> ids);

    /**
     * retrieve server ids by supervisor ids
     * @param ids supervisor id list
     * @return supervisor server list which meet the criteria
     */
    List<HokageSupervisorServerDO> listBySupervisorIds(List<Long> ids);

    /**
     * recycle server manage right by supervisorId
     * @param id supervisor id
     * @return rows affected
     */
    Long removeBySupervisorId(Long id);

    /**
     * recycle server manage right by supervisor Id and server ids
     * @param id supervisor id
     * @param serverIds server id list
     * @return rows affected
     */
    Long removeBySupervisorId(Long id, List<Long> serverIds);

    /**
     * grant servers to a supervisor
     * @param id supervisor id
     * @param serverIds server id list
     * @return rows affected
     */
    Integer addBySupervisorId(Long id, List<Long> serverIds);

    /**
     * retrieve relationship based-on supervisor id and server id
     * @param supervisorId supervisor id
     * @param serverId server id
     * @return supervisor server
     */
    HokageSupervisorServerDO queryBySupervisorIdAndServerId(Long supervisorId, Long serverId);
}
