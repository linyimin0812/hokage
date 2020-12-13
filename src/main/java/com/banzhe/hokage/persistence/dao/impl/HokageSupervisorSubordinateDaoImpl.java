package com.banzhe.hokage.persistence.dao.impl;

import com.banzhe.hokage.persistence.dao.HokageSupervisorSubordinateDao;
import com.banzhe.hokage.persistence.dataobject.HokageSupervisorSubordinateDO;
import com.banzhe.hokage.persistence.mapper.HokageSupervisorSubordinateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/8/22 11:42 上午
 * @email linyimin520812@gmail.com
 * @description 管理员与用户关系映射表
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
        return supervisorSubordinateMapper.listBySupervisorId(id);
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
}
