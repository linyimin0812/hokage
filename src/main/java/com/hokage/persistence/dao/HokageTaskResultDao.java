package com.hokage.persistence.dao;

import com.hokage.persistence.dataobject.HokageTaskResultDO;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/7/27 11:33 pm
 * @email linyimin520812@gmail.com
 * @description
 */
public interface HokageTaskResultDao {
    /**
     * insert a new task result
     * @param taskResultDO task result DO
     * @return rows affected
     */
    Long insert(HokageTaskResultDO taskResultDO);

    /**
     * update a task result
     * @param taskResultDO task result DO
     * @return rows affected
     */
    Long update(HokageTaskResultDO taskResultDO);

    /**
     * query task result based on task result primary id
     * @param id task result primary id
     * @return task result which id is equal to parameter id
     */
    HokageTaskResultDO findById(Long id);

    /**
     * query task result by task id
     * @param taskId task primary id
     * @return task result list which meet the criteria
     */
    List<HokageTaskResultDO> findByTaskId(Long taskId);

    /**
     * query task result by user id
     * @param userId user id
     * @return task result list which meet the criteria
     */
    List<HokageTaskResultDO> listByUserId(Long userId);

}
