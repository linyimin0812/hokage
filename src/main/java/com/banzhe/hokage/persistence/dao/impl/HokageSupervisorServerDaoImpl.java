package com.banzhe.hokage.persistence.dao.impl;

import com.banzhe.hokage.persistence.dao.HokageSupervisorServerDao;
import com.banzhe.hokage.persistence.dataobject.HokageSubordinateServerDO;
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
     * 查找服务器的管理者id
     * @param id
     * @return
     */
    @Override
    public List<HokageSupervisorServerDO> selectByServerId(Long id) {
        return supervisorServerMapper.selectByServerId(id);
    }

    /**
     * 查找管理员id下的服务器id
     * @param id
     * @return
     */
    @Override
    public List<HokageSupervisorServerDO> selectBySupervisorId(Long id) {
        return supervisorServerMapper.selectBySupervisorId(id);
    }
}
