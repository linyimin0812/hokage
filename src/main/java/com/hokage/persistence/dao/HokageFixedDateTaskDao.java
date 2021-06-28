package com.hokage.persistence.dao;

import com.hokage.persistence.dataobject.HokageFixedDateTaskDO;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/7/27 11:46 pm
 * @email linyimin520812@gmail.com
 * @description fixed data task dao interface
 */
public interface HokageFixedDateTaskDao {
    /**
     * insert a new fixed date task
     * @param fixedDateTaskDO task DO
     * @return rows affected
     */
    Long insert(HokageFixedDateTaskDO fixedDateTaskDO);

    /**
     * update a fixed date task
     * @param fixedDateTaskDO task DO
     * @return rows affected
     */
    Long update(HokageFixedDateTaskDO fixedDateTaskDO);

    /**
     * query task based on task id
     * @param id task primary id
     * @return task which id meet the criteria
     */
    HokageFixedDateTaskDO findById(Long id);

    /**
     * find task list by task name
     * @param name task name
     * @return task list which meet the criteria
     */
    List<HokageFixedDateTaskDO> findByName(String name);

    /**
     * list task list by task type
     * @param type task type
     * @return task list which meet the criteria
     */
    List<HokageFixedDateTaskDO> findByType(Integer type);

    /**
     * find task information based on other criteria
     * @param fixedDateTaskDO task criteria
     * @return task list which meet the criteria
     */
    List<HokageFixedDateTaskDO> findAll(HokageFixedDateTaskDO fixedDateTaskDO);

    /**
     * find task list by task name
     * @param userId user id
     * @return task list which meet the criteria
     */
    List<HokageFixedDateTaskDO> listByUserId(Long userId);

    /**
     * list task which execTime between start and end
     * @param start current time mills
     * @param end current time mills + 5 * 60 * 1000
     * @return runnable task list
     */
    List<HokageFixedDateTaskDO> listRunnableTask(Long start, Long end);
}
