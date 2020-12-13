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
     * insert a new record
     * @param subordinateServerDO
     * @return
     */
    @Override
    public Long insert(HokageSubordinateServerDO subordinateServerDO) {
        return subordinateServerMapper.insert(subordinateServerDO);
    }

    /**
     * update a record
     * @param subordinateServerDO
     * @return
     */
    @Override
    public Long update(HokageSubordinateServerDO subordinateServerDO) {
        return subordinateServerMapper.update(subordinateServerDO);
    }

    /**
     * retrieve subordinate ids based on server ids
     * @param ids
     * @return
     */
    @Override
    public List<HokageSubordinateServerDO> listByServerIds(List<Long> ids) {
        return subordinateServerMapper.selectByServerIds(ids);
    }

    /**
     * retrieve server ids based on subordinate ids
     * @param ids
     * @return
     */
    @Override
    public List<HokageSubordinateServerDO> listByOrdinateIds(List<Long> ids) {
        return subordinateServerMapper.selectByOrdinateIds(ids);
    }

    /**
     * retrieve record based on primary key
     * @param id
     * @return
     */
    @Override
    public HokageSubordinateServerDO getById(Long id) {
        return subordinateServerMapper.selectById(id);
    }
}
