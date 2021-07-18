package com.hokage.persistence.dao.impl;

import com.hokage.persistence.dao.HokageSubordinateServerDao;
import com.hokage.persistence.dataobject.HokageSubordinateServerDO;
import com.hokage.persistence.mapper.HokageSubordinateServerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
     * @param subordinateServerDO subordinate server data object
     * @return rows affected
     */
    @Override
    public Long insert(HokageSubordinateServerDO subordinateServerDO) {
        return subordinateServerMapper.insert(subordinateServerDO);
    }

    /**
     * update a record
     * @param subordinateServerDO subordinate server data object
     * @return rows affected
     */
    @Override
    public Long update(HokageSubordinateServerDO subordinateServerDO) {
        return subordinateServerMapper.update(subordinateServerDO);
    }

    @Override
    public Long upsert(HokageSubordinateServerDO subordinateServerDO) {
        HokageSubordinateServerDO subServerDO = subordinateServerMapper.queryBySubordinateIdAndServerId(subordinateServerDO.getSubordinateId(), subordinateServerDO.getServerId());
        if (Objects.isNull(subServerDO)) {
            return this.insert(subordinateServerDO);
        }
        subordinateServerDO.setId(subServerDO.getId());
        return this.update(subordinateServerDO);
    }

    @Override
    public List<HokageSubordinateServerDO> listByServerIds(List<Long> ids) {
        return Optional.ofNullable(subordinateServerMapper.selectByServerIds(ids)).orElse(Collections.emptyList());
    }

    @Override
    public List<HokageSubordinateServerDO> listByOrdinateIds(List<Long> ids) {
        return Optional.ofNullable(subordinateServerMapper.selectByOrdinateIds(ids)).orElse(Collections.emptyList());
    }

    /**
     * retrieve record based on primary key
     * @param id primary id
     * @return subordinate server which meet the criteria
     */
    @Override
    public HokageSubordinateServerDO getById(Long id) {
        return subordinateServerMapper.selectById(id);
    }

    /**
     * grant servers to a subordinate
     * @param subordinateId subordinate id
     * @param serverIds server id list
     * @return rows affected
     */
    @Override
    public Long addBySubordinateId(Long subordinateId, List<Long> serverIds) {
        return subordinateServerMapper.addBySubordinateId(subordinateId, serverIds);
    }

    @Override
    public Long removeBySubordinateId(Long id) {
        return this.removeBySubordinateId(id, Collections.emptyList());
    }

    @Override
    public Long removeBySubordinateId(Long id, List<Long> serverIds) {
        return subordinateServerMapper.removeBySubordinateId(id, serverIds);
    }

    @Override
    public HokageSubordinateServerDO queryBySubordinateIdAndServerId(Long subordinateId, Long serverId) {
        return subordinateServerMapper.queryBySubordinateIdAndServerId(subordinateId, serverId);
    }
}
