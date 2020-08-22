package com.banzhe.hokage.service;

import com.banzhe.hokage.persistence.dataobject.HokageTaskDO;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/7/27 11:46 下午
 * @email linyimin520812@gmail.com
 * @description 任务表操作
 */
public interface HokageTaskService {
    /**
     * 插入一条新的记录
     * @param hokageTaskDO
     * @return
     */
    Integer insert(HokageTaskDO hokageTaskDO);

    /**
     * 更新一条记录
     * @param hokageTaskDO
     * @return
     */
    Integer update(HokageTaskDO hokageTaskDO);

    /**
     * 根据id查找任务信息
     * @param id
     * @return
     */
    HokageTaskDO findById(Long id);

    /**
     * 根据任务名名查找任务信息
     * @param name
     * @return
     */
    HokageTaskDO findByName(String name);

    /**
     * 根据任务类型查找任务信息
     * @param type
     * @return
     */
    List<HokageTaskDO> findByType(Integer type);

    /**
     * 根据其他条件查找任务信息
     * @param hokageTaskDO
     * @return
     */
    List<HokageTaskDO> findAll(HokageTaskDO hokageTaskDO);

}
