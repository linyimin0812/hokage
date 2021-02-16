package com.hokage.persistence.dao;

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
     * @param supervisorServerDO
     * @return
     */
    Long insert(HokageSupervisorServerDO supervisorServerDO);

    /**
     * update a recode
     * @param supervisorServerDO
     * @return
     */
    Long update(HokageSupervisorServerDO supervisorServerDO);

    /**
     * retrieve supervisor id by server ids
     * @param ids
     * @return
     */
    List<HokageSupervisorServerDO> listByServerIds(List<Long> ids);

    /**
     * retrieve server ids by supervisor ids
     * @param ids
     * @return
     */
    List<HokageSupervisorServerDO> listBySupervisorIds(List<Long> ids);

    /**
     * recycle server manage right by supervisorId
     * @param id
     * @return
     */
    Integer removeBySupervisorId(Long id);

    /**
     * recycle server manage right by supervisor Id and server ids
     * @param id
     * @param serverIds
     * @return
     */
    Integer removeBySupervisorId(Long id, List<Long> serverIds);

    /**
     * grant servers to a supervisor
     * @param id
     * @param serverIds
     * @return
     */
    Integer addBySupervisorId(Long id, List<Long> serverIds);

    /**
     * retrieve relationship based-on supervisor id and server id
     * @param supervisorId
     * @param serverId
     * @return
     */
    HokageSupervisorServerDO queryBySupervisorIdAndServerId(Long supervisorId, Long serverId);
}
