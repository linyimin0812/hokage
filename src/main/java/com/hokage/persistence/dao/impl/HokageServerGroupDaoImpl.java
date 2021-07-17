package com.hokage.persistence.dao.impl;

import com.hokage.persistence.dao.HokageServerGroupDao;
import com.hokage.persistence.dataobject.HokageServerGroupDO;
import com.hokage.persistence.mapper.HokageServerGroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    @Override
    public Long insert(HokageServerGroupDO serverGroupDO) {
        return serverGroupMapper.insert(serverGroupDO);
    }

    @Override
    public List<HokageServerGroupDO> selectAll() {
        return Optional.ofNullable(serverGroupMapper.selectAll()).orElse(Collections.emptyList());
    }

    @Override
    public Long update(HokageServerGroupDO serverGroupDO) {
        return serverGroupMapper.update(serverGroupDO);
    }

    @Override
    public List<HokageServerGroupDO> listByCreatorId(Long id) {
        return Optional.ofNullable(serverGroupMapper.listByCreatorId(id)).orElse(Collections.emptyList());
    }

    @Override
    public List<HokageServerGroupDO> listByCreatorId(Long creatorId, String name) {
        return Optional.ofNullable(serverGroupMapper.listByCreatorIdAndName(creatorId, name)).orElse(Collections.emptyList());
    }

}
