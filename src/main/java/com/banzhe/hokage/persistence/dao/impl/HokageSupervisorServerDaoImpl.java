package com.banzhe.hokage.persistence.dao.impl;

import com.banzhe.hokage.persistence.dao.HokageSupervisorServerDao;
import com.banzhe.hokage.persistence.dataobject.HokageSupervisorServerDO;
import com.banzhe.hokage.persistence.mapper.HokageSupervisorServerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/8/22 12:01 下午
 * @email linyimin520812@gmail.com
 * @description
 */
@Repository
public class HokageSupervisorServerDaoImpl implements HokageSupervisorServerDao {

    private HokageSupervisorServerMapper supervisorServerMapper;

    @Autowired
    public void setSupervisorServerMapper(HokageSupervisorServerMapper supervisorServerMapper) {
        this.supervisorServerMapper = supervisorServerMapper;
    }

    /**
     * 插入一条新纪录
     * @param supervisorServerDO
     * @return
     */
    @Override
    public Long insert(HokageSupervisorServerDO supervisorServerDO) {
        return supervisorServerMapper.insert(supervisorServerDO);
    }

    /**
     * 更新一条记录
     * @param supervisorServerDO
     * @return
     */
    @Override
    public Long update(HokageSupervisorServerDO supervisorServerDO) {
        return supervisorServerMapper.update(supervisorServerDO);
    }

    /**
     * retrieve supervisor id by server ids
     * @param ids: server ids
     * @return
     */
    @Override
    public List<HokageSupervisorServerDO> listByServerIds(List<Long> ids) {
        return supervisorServerMapper.listByServerIds(ids);
    }

    /**
     * retrieve server id by supervisor ids
     * @param ids: supervisor ids
     * @return
     */
    @Override
    public List<HokageSupervisorServerDO> listBySupervisorIds(List<Long> ids) {
        return supervisorServerMapper.listBySupervisorIds(ids);
    }

    @Override
    public Integer removeBySupervisorId(Long id) {
        return supervisorServerMapper.removeBySupervisorId(id);
    }

    @Override
    public Integer removeBySupervisorId(Long id, List<Long> serverIds) {
        return supervisorServerMapper.removeBySupervisorId(id, serverIds);
    }

    @Override
    public Integer addBySupervisorId(Long id, List<Long> serverIds) {
        return supervisorServerMapper.addBySupervisorId(id, serverIds);
    }
}
