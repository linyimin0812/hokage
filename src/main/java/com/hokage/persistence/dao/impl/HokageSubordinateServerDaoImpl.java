package com.hokage.persistence.dao.impl;

import com.hokage.persistence.dao.HokageSubordinateServerDao;
import com.hokage.persistence.dataobject.HokageSubordinateServerDO;
import com.hokage.persistence.mapper.HokageSubordinateServerMapper;
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

    /**
     * grant servers to a subordinate
     * @param subordinateId
     * @param serverIds
     * @return
     */
    @Override
    public Long addBySubordinateId(Long subordinateId, List<Long> serverIds) {
        return subordinateServerMapper.addBySubordinateId(subordinateId, serverIds);
    }

    @Override
    public Integer removeBySubordinateId(Long id) {
        return subordinateServerMapper.removeBySubordinateId(id);
    }

    @Override
    public Integer removeBySubordinateId(Long id, List<Long> serverIds) {
        return subordinateServerMapper.removeBySubordinateId(id, serverIds);
    }

    @Override
    public HokageSubordinateServerDO queryBySubordinateIdAndServerId(Long subordinateId, Long serverId) {
        return subordinateServerMapper.queryBySubordinateIdAndServerId(subordinateId, serverId);
    }
}