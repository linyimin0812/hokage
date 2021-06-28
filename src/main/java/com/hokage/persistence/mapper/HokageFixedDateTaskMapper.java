package com.hokage.persistence.mapper;

import com.hokage.persistence.dataobject.HokageFixedDateTaskDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/7/27 11:46 下午
 * @email linyimin520812@gmail.com
 * @description 任务表操作
 */
@Mapper
@Component
public interface HokageFixedDateTaskMapper {
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
    HokageFixedDateTaskDO findById(@Param("id") Long id);

    /**
     * find task list by task name
     * @param name task name
     * @return task list which meet the criteria
     */
    List<HokageFixedDateTaskDO> findByName(@Param("taskName") String name);

    /**
     * list task list by task type
     * @param type task type
     * @return task list which meet the criteria
     */
    List<HokageFixedDateTaskDO> findByType(@Param("type") Integer type);

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
    List<HokageFixedDateTaskDO> listByUserId(@Param("userId") Long userId);

    /**
     * list task which execTime between start and end
     * @param start current time mills
     * @param end current time mills + 5 * 60 * 1000
     * @return runnable task list
     */
    List<HokageFixedDateTaskDO> listRunnableTask(@Param("start") Long start, @Param("end") Long end);

}
