package com.hokage.persistence.dao.impl;

import com.hokage.persistence.dao.HokageFixedDateTaskDao;
import com.hokage.persistence.dataobject.HokageFixedDateTaskDO;
import com.hokage.persistence.mapper.HokageFixedDateTaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author linyimin
 * @date 2020/7/27 11:46 pm
 * @email linyimin520812@gmail.com
 * @description fixed data task dao interface implementation
 */
@Repository
public class HokageTaskDaoImpl implements HokageFixedDateTaskDao {

    private HokageFixedDateTaskMapper taskMapper;

    @Autowired
    public void setTaskMapper(HokageFixedDateTaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    @Override
    public Long insert(HokageFixedDateTaskDO hokageTaskDO) {
        return taskMapper.insert(hokageTaskDO);
    }


    @Override
    public Long update(HokageFixedDateTaskDO hokageTaskDO) {
        return taskMapper.update(hokageTaskDO);
    }

    @Override
    public HokageFixedDateTaskDO findById(Long id) {
        return taskMapper.findById(id);
    }


    @Override
    public List<HokageFixedDateTaskDO> findByName(String name) {
        return Optional.ofNullable(taskMapper.findByName(name)).orElse(Collections.emptyList());
    }

    @Override
    public List<HokageFixedDateTaskDO> findByType(Integer type) {
        return Optional.ofNullable(taskMapper.findByType(type)).orElse(Collections.emptyList());
    }

    @Override
    public List<HokageFixedDateTaskDO> findAll(HokageFixedDateTaskDO hokageTaskDO) {
        return Optional.ofNullable(taskMapper.findAll(hokageTaskDO)).orElse(Collections.emptyList());
    }

    @Override
    public List<HokageFixedDateTaskDO> listByUserId(Long userId) {
        return Optional.ofNullable(taskMapper.listByUserId(userId)).orElse(Collections.emptyList());
    }

}
