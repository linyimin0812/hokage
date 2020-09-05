package com.banzhe.hokage.persistence.dao.impl;

import com.banzhe.hokage.persistence.dao.HokageTaskResultDao;
import com.banzhe.hokage.persistence.dataobject.HokageTaskResultDO;
import com.banzhe.hokage.persistence.mapper.HokageTaskResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/7/27 11:33 下午
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
    /**
     * 插入一条任务执行结果
     * @param hokageTaskResultDO
     * @return
     */
    @Override
    public Long insert(HokageTaskResultDO hokageTaskResultDO) {
        return taskResultMapper.insert(hokageTaskResultDO);
    }

    /**
     * 更新一条任务执行结果
     * @param hokageTaskResultDO
     * @return
     */
    @Override
    public Long update(HokageTaskResultDO hokageTaskResultDO) {
        return taskResultMapper.update(hokageTaskResultDO);
    }

    /**
     * 根据id主键查找任务执行结果
     * @param id
     * @return
     */
    public HokageTaskResultDO findById(Long id) {
        return taskResultMapper.findById(id);
    }

    /**
     * 根据任务id查找任务执行结果
     * @param taskId
     * @return
     */
    @Override
    public List<HokageTaskResultDO> findByTaskId(Long taskId) {
        return taskResultMapper.findByTaskId(taskId);
    }
}
