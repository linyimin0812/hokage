package com.hokage.persistence.dao;

import com.hokage.biz.response.server.HokageServerVO;
import com.hokage.persistence.dataobject.HokageSubordinateServerDO;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/8/22 11:56 am
 * @email linyimin520812@gmail.com
 * @description
 */
public interface HokageSubordinateServerDao {

    /**
     * insert a new record
     * @param subordinateServerDO subordinate server data object
     * @return rows affected
     */
    Long insert(HokageSubordinateServerDO subordinateServerDO);

    /**
     * update a record
     * @param subordinateServerDO subordinate server data object
     * @return rows affected
     */
    Long update(HokageSubordinateServerDO subordinateServerDO);

    /**
     * insert or update a record
     * @param subordinateServerDO subordinate server do
     * @return rows affected
     */
    Long upsert(HokageSubordinateServerDO subordinateServerDO);

    /**
     * retrieve subordinate ids based on server ids
     * @param ids server id list
     * @return subordinate server list which meet the criteria
     */
    List<HokageSubordinateServerDO> listByServerIds(List<Long> ids);

    /**
     * retrieve server ids based on subordinate ids
     * @param ids subordinate id list
     * @return subordinate server list which meet the criteria
     */
    List<HokageSubordinateServerDO> listByOrdinateIds(List<Long> ids);

    /**
     * retrieve record based on primary key
     * @param id primary id
     * @return subordinate server which meet the criteria
     */
    HokageSubordinateServerDO getById(Long id);

    /**
     * grant servers to a subordinate
     * @param subordinateId subordinate id
     * @param serverIds server id list
     * @return rows affected
     */
    Long addBySubordinateId(Long subordinateId, List<Long> serverIds);

    /**
     * recycle server manage right by supervisorId
     * @param id subordinate id
     * @return rows affected
     */
    Long removeBySubordinateId(Long id);

    /**
     * recycle server manage right by supervisor Id and server ids
     * @param id subordinate primary id
     * @param serverIds server id list
     * @return rows affected
     */
    Long removeBySubordinateId(Long id, List<Long> serverIds);

    /**
     * retrieve relationship based-on subordinate id and server id
     * @param subordinateId subordinate id
     * @param serverId server id
     * @return subordinate server data object
     */
    HokageSubordinateServerDO queryBySubordinateIdAndServerId(Long subordinateId, Long serverId);
}
