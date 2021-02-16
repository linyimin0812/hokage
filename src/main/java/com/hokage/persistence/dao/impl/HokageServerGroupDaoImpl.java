package com.hokage.persistence.dao.impl;

import com.hokage.persistence.dao.HokageServerGroupDao;
import com.hokage.persistence.dataobject.HokageServerGroupDO;
import com.hokage.persistence.mapper.HokageServerGroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/8/22 11:14 上午
 * @email linyimin520812@gmail.com
 * @description 服务器组信息
 */
@Repository
public class HokageServerGroupDaoImpl implements HokageServerGroupDao {

    private HokageServerGroupMapper serverGroupMapper;

    @Autowired
    public void setServerGroupMapper(HokageServerGroupMapper serverGroupMapper) {
        this.serverGroupMapper = serverGroupMapper;
    }

    /**
     * 插入一条新的记录
     * @param serverGroupDO
     * @return
     */
    @Override
    public Long insert(HokageServerGroupDO serverGroupDO) {
        return serverGroupMapper.insert(serverGroupDO);
    }

    /**
     * 返回所有服务器组信息
     * @return
     */
    @Override
    public List<HokageServerGroupDO> selectAll() {
        return serverGroupMapper.selectAll();
    }

    /**
     * 更新服务器组信息
     * @param serverGroupDO
     * @return
     */
    @Override
    public Long update(HokageServerGroupDO serverGroupDO) {
        return serverGroupMapper.update(serverGroupDO);
    }

}
