package com.hokage.persistence.dao;

import com.hokage.common.ServiceResponse;
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
     * @param serverGroupDO
     * @return
     */
    Long insert(HokageServerGroupDO serverGroupDO);

    /**
     * list all server group
     * @return
     */
    List<HokageServerGroupDO> selectAll();

    /**
     * update
     * @param serverGroupDO
     * @return
     */
    Long update(HokageServerGroupDO serverGroupDO);

    /**
     * list server group based-on creator id
     * @param id
     * @return
     */
    List<HokageServerGroupDO> listByCreatorId(Long id);

    /**
     * list server group based-on creator id and group name
     * @param creatorId
     * @param name
     * @return
     */
    List<HokageServerGroupDO> listByCreatorId(Long creatorId, String name);
}
