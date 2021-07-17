package com.hokage.persistence.dao;

import com.hokage.persistence.dataobject.HokageServerGroupDO;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/8/22 11:14 am
 * @email linyimin520812@gmail.com
 * @description server group dao interface
 */
public interface HokageServerGroupDao {
    /**
     * insert a new record
     * @param serverGroupDO server group data pbject
     * @return rows affected
     */
    Long insert(HokageServerGroupDO serverGroupDO);

    /**
     * list all server group
     * @return {@link List<HokageServerGroupDO>}
     */
    List<HokageServerGroupDO> selectAll();

    /**
     * update a server group record
     * @param serverGroupDO server group data pbject
     * @return rows affected
     */
    Long update(HokageServerGroupDO serverGroupDO);

    /**
     * list server group based-on creator id
     * @param id creator id
     * @return {@link List<HokageServerGroupDO>}
     */
    List<HokageServerGroupDO> listByCreatorId(Long id);

    /**
     * list server group based-on creator id and group name
     * @param creatorId creator id
     * @param name group name
     * @return {@link List<HokageServerGroupDO>}
     */
    List<HokageServerGroupDO> listByCreatorId(Long creatorId, String name);
}
