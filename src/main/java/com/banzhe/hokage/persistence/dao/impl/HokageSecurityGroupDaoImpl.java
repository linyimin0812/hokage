package com.banzhe.hokage.persistence.dao.impl;

import com.banzhe.hokage.persistence.dao.HokageSecurityGroupDao;
import com.banzhe.hokage.persistence.dataobject.HokageSecurityGroupDO;
import com.banzhe.hokage.persistence.mapper.HokageSecurityGroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/8/22 11:19 上午
 * @email linyimin520812@gmail.com
 * @description 服务器安全组信息
 */
@Component
public class HokageSecurityGroupDaoImpl implements HokageSecurityGroupDao {

    private HokageSecurityGroupMapper securityGroupMapper;

    @Autowired
    public void setSecurityGroupMapper(HokageSecurityGroupMapper securityGroupMapper) {
        this.securityGroupMapper = securityGroupMapper;
    }

    /**
     * 插入一条信息记录
     * @param securityGroupDO
     * @return
     */
    @Override
    public Long insert(HokageSecurityGroupDO securityGroupDO) {
        return securityGroupMapper.insert(securityGroupDO);
    }

    /**
     * 更新服务器安全组信息
     * @param securityGroupDO
     * @return
     */
    @Override
    public Long update(HokageSecurityGroupDO securityGroupDO) {
        return securityGroupMapper.update(securityGroupDO);
    }

    /**
     * 获取所有安全组信息
     * @return
     */
    @Override
    public List<HokageSecurityGroupDO> selectAll() {
        return securityGroupMapper.selectAll();
    }
}
