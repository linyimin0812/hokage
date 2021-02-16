package com.hokage.persistence.dao.impl;

import com.hokage.persistence.dao.HokageTaskDao;
import com.hokage.persistence.dataobject.HokageTaskDO;
import com.hokage.persistence.mapper.HokageTaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/7/27 11:46 下午
 * @email linyimin520812@gmail.com
 * @description 任务表操作
 */
@Repository
public class HokageTaskDaoImpl implements HokageTaskDao {

    private HokageTaskMapper taskMapper;

    @Autowired
    public void setTaskMapper(HokageTaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }
    /**
     * 插入一条新的记录
     * @param hokageTaskDO
     * @return
     */
    public Long insert(HokageTaskDO hokageTaskDO) {
        return taskMapper.insert(hokageTaskDO);
    }

    /**
     * 更新一条记录
     * @param hokageTaskDO
     * @return
     */
    @Override
    public Long update(HokageTaskDO hokageTaskDO) {
        return taskMapper.update(hokageTaskDO);
    }

    /**
     * 根据id查找任务信息
     * @param id
     * @return
     */
    @Override
    public HokageTaskDO findById(Long id) {
        return taskMapper.findById(id);
    }

    /**
     * 根据任务名名查找任务信息
     * @param name
     * @return
     */
    @Override
    public HokageTaskDO findByName(String name) {
        return taskMapper.findByName(name);
    }

    /**
     * 根据任务类型查找任务信息
     * @param type
     * @return
     */
    @Override
    public List<HokageTaskDO> findByType(Integer type) {
        return taskMapper.findByType(type);
    }

    /**
     * 根据其他条件查找任务信息
     * @param hokageTaskDO
     * @return
     */
    @Override
    public List<HokageTaskDO> findAll(HokageTaskDO hokageTaskDO) {
        return taskMapper.findAll(hokageTaskDO);
    }

}
