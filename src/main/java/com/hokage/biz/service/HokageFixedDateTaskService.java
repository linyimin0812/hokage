package com.hokage.biz.service;

import com.hokage.biz.response.bat.HokageFixedDateTaskVO;
import com.hokage.common.ServiceResponse;
import com.hokage.persistence.dataobject.HokageFixedDateTaskDO;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/7/27 11:46 pm
 * @email linyimin520812@gmail.com
 * @description fixed date task service interface
 */
public interface HokageFixedDateTaskService {
    /**
     * insert or update a new fixed date task record
     * @param fixedDateTaskDO task object
     * @return primary key
     */
    ServiceResponse<Long> upsert(HokageFixedDateTaskDO fixedDateTaskDO);

    /**
     * query task based on task id
     * @param id task primary id
     * @return task which id meet the criteria
     */
    ServiceResponse<HokageFixedDateTaskVO> findById(Long id);

    /**
     * find task list by task name
     * @param name task name
     * @return task list which meet the criteria
     */
    ServiceResponse<List<HokageFixedDateTaskDO>> findByName(String name);

    /**
     * list task list by task type
     * @param type task type
     * @return task list which meet the criteria
     */
    ServiceResponse<List<HokageFixedDateTaskDO>> findByType(Integer type);

    /**
     * find task information based on other criteria
     * @param fixedDateTaskDO task criteria
     * @return task list which meet the criteria
     */
    ServiceResponse<List<HokageFixedDateTaskDO>> findAll(HokageFixedDateTaskDO fixedDateTaskDO);

    /**
     * list task list by task type
     * @param userId user id
     * @return task list which meet the criteria
     */
    ServiceResponse<List<HokageFixedDateTaskVO>> listByUserId(Long userId);

    /**
     * delete task by primary id
     * @param id fixed date task primary id
     * @return true: delete success, false: delete false
     */
    ServiceResponse<Boolean> deleteById(Long id);

    /**
     * offline task by primary id
     * @param id fixed date task primary id
     * @return true: offline success, false: offline false
     */
    ServiceResponse<Boolean> offline(Long id);

    /**
     * online task by primary id
     * @param id fixed date task primary id
     * @return true: online success, false: online false
     */
    ServiceResponse<Boolean> online(Long id);
}
