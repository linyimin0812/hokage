package com.hokage.persistence.dao.impl;

import com.hokage.biz.enums.RecordStatusEnum;
import com.hokage.persistence.dao.HokageSupervisorServerDao;
import com.hokage.persistence.dataobject.HokageSupervisorServerDO;
import com.hokage.persistence.dataobject.HokageSupervisorSubordinateDO;
import com.hokage.persistence.mapper.HokageSupervisorServerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author linyimin
 * @date 2020/8/22 12:01 pm
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
        return Optional.ofNullable(supervisorServerMapper.listByServerIds(ids)).orElse(Collections.emptyList());
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
    public Long removeBySupervisorId(Long id) {
        HokageSupervisorServerDO supervisorServerDO = new HokageSupervisorServerDO();
        supervisorServerDO.setSupervisorId(id);
        supervisorServerDO.setStatus(RecordStatusEnum.deleted.getStatus());
        return this.update(supervisorServerDO);
    }

    @Override
    public Long removeBySupervisorId(Long id, List<Long> serverIds) {
        return serverIds.stream().map(serverId -> {
            HokageSupervisorServerDO supervisorServerDO = new HokageSupervisorServerDO();
            supervisorServerDO.setServerId(serverId);
            supervisorServerDO.setSupervisorId(id);
            supervisorServerDO.setStatus(RecordStatusEnum.deleted.getStatus());
            return this.update(supervisorServerDO);
        }).reduce(0L, Long::sum);
    }

    @Override
    public Integer addBySupervisorId(Long id, List<Long> serverIds) {
        return supervisorServerMapper.addBySupervisorId(id, serverIds);
    }

    @Override
    public HokageSupervisorServerDO queryBySupervisorIdAndServerId(Long supervisorId, Long serverId) {
        return supervisorServerMapper.queryBySupervisorIdAndServerId(supervisorId, serverId);
    }
}
