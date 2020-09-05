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
     * 插入一条新纪录
     * @param supervisorSubordinateDO
     * @return
     */
    @Override
    public Long insert(HokageSupervisorSubordinateDO supervisorSubordinateDO) {
        return supervisorSubordinateMapper.insert(supervisorSubordinateDO);
    }

    /**
     * 更新一条记录
     * @param supervisorSubordinateDO
     * @return
     */
    @Override
    public Long update(HokageSupervisorSubordinateDO supervisorSubordinateDO) {
        return supervisorSubordinateMapper.update(supervisorSubordinateDO);
    }

    /**
     * 根据id查找管理员与用户的关系映射信息
     * @param id
     * @return
     */
    @Override
    public HokageSupervisorSubordinateDO selectById(Long id) {
        return supervisorSubordinateMapper.selectById(id);
    }

    /**
     * 查找全部信息
     * @return
     */
    @Override
    public List<HokageSupervisorSubordinateDO> selectAll() {
        return supervisorSubordinateMapper.selectAll();
    }

    /**
     * 查找管理员名下的用户id
     * @param id
     * @return
     */
    public List<HokageSupervisorSubordinateDO> selectBySupervisorId(Long id) {
        return supervisorSubordinateMapper.selectBySupervisorId(id);
    }

    /**
     * 查找用户对应的管理员id
     * @param id
     * @return
     */
    public List<HokageSupervisorSubordinateDO> selectBySubordinateId(Long id) {
        return supervisorSubordinateMapper.selectBySubordinateId(id);
    }
}
