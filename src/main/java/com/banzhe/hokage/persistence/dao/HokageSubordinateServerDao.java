package com.banzhe.hokage.persistence.dao;

import com.banzhe.hokage.persistence.dataobject.HokageSubordinateServerDO;
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
     * @param subordinateServerDO
     * @return
     */
    Long insert(HokageSubordinateServerDO subordinateServerDO);

    /**
     * update a record
     * @param subordinateServerDO
     * @return
     */
    Long update(HokageSubordinateServerDO subordinateServerDO);

    /**
     * retrieve subordinate ids based on server ids
     * @param ids
     * @return
     */
    List<HokageSubordinateServerDO> listByServerIds(List<Long> ids);

    /**
     * retrieve server ids based on subordinate ids
     * @param ids
     * @return
     */
    List<HokageSubordinateServerDO> listByOrdinateIds(List<Long> ids);

    /**
     * retrieve record based on primary key
     * @param id
     * @return
     */
    HokageSubordinateServerDO getById(Long id);

    /**
     * grant servers to a subordinate
     * @param subordinateId
     * @param serverIds
     * @return
     */
    Long addBySubordinateId(Long subordinateId, List<Long> serverIds);

    /**
     * recycle server manage right by supervisorId
     * @param id
     * @return
     */
    Integer removeBySubordinateId(Long id);

    /**
     * recycle server manage right by supervisor Id and server ids
     * @param id
     * @param serverIds
     * @return
     */
    Integer removeBySubordinateId(Long id, List<Long> serverIds);

}
