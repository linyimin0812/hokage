package com.banzhe.hokage.persistence.dao.impl;

import com.banzhe.hokage.persistence.dao.HokageServerDao;
import com.banzhe.hokage.persistence.dataobject.HokageServerDO;
import com.banzhe.hokage.persistence.mapper.HokageServerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/8/22 11:28 上午
 * @email linyimin520812@gmail.com
 * @description 服务器信息
 */
@Repository
public class HokageServerDaoImpl implements HokageServerDao {

    private HokageServerMapper serverMapper;

    @Autowired
    public void setServerMapper(HokageServerMapper serverMapper) {
        this.serverMapper = serverMapper;
    }

    /**
     * 插入一条新记录
     * @param serverDO
     * @return
     */
    @Override
    public Long insert(HokageServerDO serverDO) {
        return serverMapper.insert(serverDO);
    }

    /**
     * 更新一条记录
     * @param serverDO
     * @return
     */
    @Override
    public Long update(HokageServerDO serverDO) {
        return serverMapper.update(serverDO);
    }

    /**
     * 获取所有服务器信息
     * @return
     */
    @Override
    public List<HokageServerDO> selectAll() {
        return serverMapper.selectAll();
    }

    /**
     * 根据id获取批量服务器
     * @param ids
     * @return
     */
    @Override
    public List<HokageServerDO> selectByIds(List<Long> ids) {
        return serverMapper.selectByIds(ids);
    }

    /**
     * 根据Id获取服务器信息
     * @param id
     * @return
     */
    @Override
    public HokageServerDO selectById(Long id) {
        return serverMapper.selectById(id);
    }

    /**
     * 根据类型查找服务器信息
     * @param type
     * @return
     */
    @Override
    public List<HokageServerDO> listByType(String type) {
        return serverMapper.selectByType(type);
    }

    /**
     * 根据服务器组查找服务器信息
     * @param group
     * @return
     */
    @Override
    public List<HokageServerDO> selectByGroup(String group) {
        return serverMapper.selectByGroup(group);
    }
}
