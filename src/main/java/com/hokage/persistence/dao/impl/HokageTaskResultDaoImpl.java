package com.hokage.persistence.dao.impl;

import com.hokage.persistence.dao.HokageTaskResultDao;
import com.hokage.persistence.dataobject.HokageTaskResultDO;
import com.hokage.persistence.mapper.HokageTaskResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author linyimin
 * @date 2020/7/27 11:33 pm
 * @email linyimin520812@gmail.com
 * @description
 */
@Repository
public class HokageTaskResultDaoImpl implements HokageTaskResultDao {

    private HokageTaskResultMapper taskResultMapper;

    @Autowired
    public void setTaskResultMapper(HokageTaskResultMapper taskResultMapper) {
        this.taskResultMapper = taskResultMapper;
    }

    @Override
    public Long insert(HokageTaskResultDO hokageTaskResultDO) {
        return taskResultMapper.insert(hokageTaskResultDO);
    }

    @Override
    public Long update(HokageTaskResultDO hokageTaskResultDO) {
        return taskResultMapper.update(hokageTaskResultDO);
    }

    @Override
    public HokageTaskResultDO findById(Long id) {
        return taskResultMapper.findById(id);
    }

    @Override
    public List<HokageTaskResultDO> findByTaskId(Long taskId) {
        return Optional.ofNullable(taskResultMapper.findByTaskId(taskId)).orElse(Collections.emptyList());
    }

    @Override
    public List<HokageTaskResultDO> listByUserId(Long userId) {
        return Optional.ofNullable(taskResultMapper.listByUserId(userId)).orElse(Collections.emptyList());
    }

    @Override
    public List<HokageTaskResultDO> listByBatchId(String batchId) {
        return Optional.ofNullable(taskResultMapper.listByBatchId(batchId)).orElse(Collections.emptyList());
    }

}
