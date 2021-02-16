package com.hokage.persistence.dao.impl;

import com.hokage.persistence.dao.HokageSequenceDao;
import com.hokage.persistence.dataobject.HokageSequenceDO;
import com.hokage.persistence.mapper.HokageSequenceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author linyimin
 * @date 2020/8/30 4:37 pm
 * @email linyimin520812@gmail.com
 * @description
 */
@Repository
public class HokageSequenceDaoImpl implements HokageSequenceDao {

    private HokageSequenceMapper sequenceMapper;

    @Autowired
    public void setSequenceMapper(HokageSequenceMapper sequenceMapper) {
        this.sequenceMapper = sequenceMapper;
    }


    @Override
    public Integer insert(HokageSequenceDO sequenceDO) {
        return sequenceMapper.insert(sequenceDO);
    }

    @Override
    public Integer update(HokageSequenceDO sequenceDO) {
        return sequenceMapper.update(sequenceDO);
    }

    @Override
    public HokageSequenceDO getSequenceByName(String name) {
        return sequenceMapper.getSequenceByName(name);
    }
}
