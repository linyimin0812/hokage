package com.banzhe.hokage.persistence.dao.impl;

import com.banzhe.hokage.persistence.dao.HokageSubordinateServerDao;
import com.banzhe.hokage.persistence.dataobject.HokageSubordinateServerDO;
import com.banzhe.hokage.persistence.mapper.HokageSubordinateServerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/8/22 11:56 上午
 * @email linyimin520812@gmail.com
 * @description
 */
@Repository
public class HokageSubordinateServerDaoImpl implements HokageSubordinateServerDao {

    private HokageSubordinateServerMapper subordinateServerMapper;

    @Autowired
    public void setSubordinateServerMapper(HokageSubordinateServerMapper subordinateServerMapper) {
        this.subordinateServerMapper = subordinateServerMapper;
    }

    /**
     * 插入一条新纪录
     * @param subordinateServerDO
     * @return
     */
    @Override
    public Long insert(HokageSubordinateServerDO subordinateServerDO) {
        return subordinateServerMapper.insert(subordinateServerDO);
    }

    /**
     * 更新一条记录
     * @param subordinateServerDO
     * @return
     */
    @Override
    public Long update(HokageSubordinateServerDO subordinateServerDO) {
        return subordinateServerMapper.update(subordinateServerDO);
    }

    /**
     * 查找服务器的使用者id
     * @param id
     * @return
     */
    @Override
    public List<HokageSubordinateServerDO> listByServerId(Long id) {
        return subordinateServerMapper.selectByServerId(id);
    }

    /**
     * 查找使用者id下的服务器id
     * @param id
     * @return
     */
    @Override
    public List<HokageSubordinateServerDO> listByOrdinateId(Long id) {
        return subordinateServerMapper.selectByOrdinateId(id);
    }

    /**
     * 使用id查找记录
     * @param id
     * @return
     */
    @Override
    public HokageSubordinateServerDO getById(Long id) {
        return subordinateServerMapper.selectById(id);
    }
}
