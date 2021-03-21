package com.hokage.persistence.dao.impl;

import com.hokage.persistence.dao.HokageServerGroupDao;
import com.hokage.persistence.dataobject.HokageServerGroupDO;
import com.hokage.persistence.mapper.HokageServerGroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @author linyimin
 * @date 2020/8/22 11:14 am
 * @email linyimin520812@gmail.com
 * @description server group dao implementation
 */
@Repository
public class HokageServerGroupDaoImpl implements HokageServerGroupDao {

    private HokageServerGroupMapper serverGroupMapper;

    @Autowired
    public void setServerGroupMapper(HokageServerGroupMapper serverGroupMapper) {
        this.serverGroupMapper = serverGroupMapper;
    }

    /**
     * insert a new record
     * @param serverGroupDO
     * @return
     */
    @Override
    public Long insert(HokageServerGroupDO serverGroupDO) {
        return serverGroupMapper.insert(serverGroupDO);
    }

    /**
     * list all server group
     * @return
     */
    @Override
    public List<HokageServerGroupDO> selectAll() {
        return serverGroupMapper.selectAll();
    }

    /**
     * update server group
     * @param serverGroupDO
     * @return
     */
    @Override
    public Long update(HokageServerGroupDO serverGroupDO) {
        return serverGroupMapper.update(serverGroupDO);
    }

    @Override
    public List<HokageServerGroupDO> listByCreatorId(Long id) {
        return serverGroupMapper.listByCreatorId(id);
    }

    @Override
    public List<HokageServerGroupDO> listByCreatorId(Long creatorId, String name) {
        List<HokageServerGroupDO> serverGroupDOList = serverGroupMapper.listByCreatorIdAndName(creatorId, name);

        if (CollectionUtils.isEmpty(serverGroupDOList)) {
            return Collections.emptyList();
        }
        return serverGroupDOList;
    }

}
