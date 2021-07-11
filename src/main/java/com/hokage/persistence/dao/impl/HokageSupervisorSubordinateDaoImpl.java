package com.hokage.persistence.dao.impl;

import com.hokage.persistence.dao.HokageSupervisorSubordinateDao;
import com.hokage.persistence.dataobject.HokageSupervisorSubordinateDO;
import com.hokage.persistence.mapper.HokageSupervisorSubordinateMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author linyimin
 * @date 2020/8/22 11:42 am
 * @email linyimin520812@gmail.com
 * @description supervisor and subordinate relationship mapping table Dao interface implementation
 */
@Repository
public class HokageSupervisorSubordinateDaoImpl implements HokageSupervisorSubordinateDao {

    private HokageSupervisorSubordinateMapper supervisorSubordinateMapper;

    @Autowired
    public void setSupervisorSubordinateMapper(HokageSupervisorSubordinateMapper supervisorSubordinateMapper) {
        this.supervisorSubordinateMapper = supervisorSubordinateMapper;
    }

    /**
     * insert a new record
     * @param supervisorSubordinateDO
     * @return
     */
    @Override
    public Long insert(HokageSupervisorSubordinateDO supervisorSubordinateDO) {
        return supervisorSubordinateMapper.insert(supervisorSubordinateDO);
    }

    /**
     * insert multiple new records
     * @param supervisorSubordinateDOS
     * @return
     */
    @Override
    public Long insertBatch(List<HokageSupervisorSubordinateDO> supervisorSubordinateDOS) {
        if (CollectionUtils.isEmpty(supervisorSubordinateDOS)) {
            return 0L;
        }
        return supervisorSubordinateMapper.insertBatch(supervisorSubordinateDOS);
    }

    /**
     * update a record
     * @param supervisorSubordinateDO
     * @return
     */
    @Override
    public Long update(HokageSupervisorSubordinateDO supervisorSubordinateDO) {
        return supervisorSubordinateMapper.update(supervisorSubordinateDO);
    }

    /**
     * retrieve the relationship mapping information between supervisor and subordinate user based on id
     * @param id
     * @return
     */
    @Override
    public HokageSupervisorSubordinateDO listById(Long id) {
        return supervisorSubordinateMapper.listById(id);
    }

    /**
     * list all mapping information
     * @return
     */
    @Override
    public List<HokageSupervisorSubordinateDO> listAll() {
        return supervisorSubordinateMapper.listAll();
    }

    /**
     * retrieve mapping information based on supervisor id
     * @param id
     * @return
     */
    @Override
    public List<HokageSupervisorSubordinateDO> listBySupervisorId(Long id) {
        return Optional.ofNullable(supervisorSubordinateMapper.listBySupervisorId(id)).orElse(Collections.emptyList());
    }

    /**
     * retrieve mapping information based on subordinate id
     * @param id
     * @return
     */
    @Override
    public List<HokageSupervisorSubordinateDO> listBySubordinateId(Long id) {
        return supervisorSubordinateMapper.listBySubordinateId(id);
    }

    /**
     * delete subordinate based on subordinate ids and supervisor id
     * @param supervisorId
     * @param subordinateIds
     * @return
     */
    @Override
    public Long deleteSubordinate(Long supervisorId, List<Long> subordinateIds) {
        return supervisorSubordinateMapper.deleteSubordinate(supervisorId, subordinateIds);
    }

    @Override
    public List<HokageSupervisorSubordinateDO> listSubordinate(Long supervisorId, List<Long> subordinateIds) {
        if (ObjectUtils.defaultIfNull(supervisorId, 0L) == 0 || CollectionUtils.isEmpty(subordinateIds)) {
            return Collections.emptyList();
        }
        return supervisorSubordinateMapper.listSubordinate(supervisorId, subordinateIds);
    }

    @Override
    public Long deleteSupervisor(Long id) {
        return supervisorSubordinateMapper.deleteSupervisor(id);
    }
}
