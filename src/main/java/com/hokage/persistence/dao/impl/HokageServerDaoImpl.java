package com.hokage.persistence.dao.impl;

import com.hokage.biz.request.AllServerQuery;
import com.hokage.biz.request.SubordinateServerQuery;
import com.hokage.biz.request.SupervisorServerQuery;
import com.hokage.persistence.dao.HokageServerDao;
import com.hokage.persistence.dataobject.HokageServerDO;
import com.hokage.persistence.mapper.HokageServerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author linyimin
 * @date 2020/8/22 11:28 am
 * @email linyimin520812@gmail.com
 * @description server info dao interface implementation
 */
@Repository
public class HokageServerDaoImpl implements HokageServerDao {

    private HokageServerMapper serverMapper;

    @Autowired
    public void setServerMapper(HokageServerMapper serverMapper) {
        this.serverMapper = serverMapper;
    }

    @Override
    public Long insert(HokageServerDO serverDO) {
        return serverMapper.insert(serverDO);
    }

    @Override
    public Long update(HokageServerDO serverDO) {
        return serverMapper.update(serverDO);
    }

    @Override
    public List<HokageServerDO> selectAll() {
        List<HokageServerDO> serverDOList = serverMapper.selectAll();
        if (CollectionUtils.isEmpty(serverDOList)) {
            return Collections.emptyList();
        }
        return serverDOList;
    }

    @Override
    public List<HokageServerDO> selectByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        return serverMapper.selectByIds(ids);
    }

    @Override
    public HokageServerDO selectById(Long id) {
        return serverMapper.selectById(id);
    }


    @Override
    public List<HokageServerDO> listByType(String type) {
        return Collections.emptyList();
    }


    @Override
    public List<HokageServerDO> selectByGroup(String group) {
        return serverMapper.selectByGroup(group);
    }

    @Override
    public List<HokageServerDO> selectBySupervisorId(Long supervisorId) {
        return Optional.ofNullable(serverMapper.selectBySupervisorId(supervisorId)).orElse(Collections.emptyList());
    }

    @Override
    public List<HokageServerDO> selectByQuery(AllServerQuery query) {
        return serverMapper.selectByAllServerQuery(query);
    }

    @Override
    public List<HokageServerDO> selectByQuery(SupervisorServerQuery query) {
        return serverMapper.selectBySupervisorServerQuery(query);
    }

    @Override
    public List<HokageServerDO> selectByQuery(SubordinateServerQuery query) {
        return serverMapper.selectBySubordinateServerQuery(query);
    }

    @Override
    public List<HokageServerDO> selectByUserId(Long userId) {
        return Optional.ofNullable(serverMapper.selectByUserId(userId)).orElse(Collections.emptyList());
    }
}
