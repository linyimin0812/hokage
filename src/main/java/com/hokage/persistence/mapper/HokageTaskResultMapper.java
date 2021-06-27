package com.hokage.persistence.mapper;

import com.hokage.persistence.dataobject.HokageTaskResultDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/7/27 11:33 pm
 * @email linyimin520812@gmail.com
 * @description
 */
@Mapper
@Component
public interface HokageTaskResultMapper {
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
    HokageTaskResultDO findById(@Param("id") Long id);

    /**
     * query task result by task id
     * @param taskId task primary id
     * @return task result list which meet the criteria
     */
    List<HokageTaskResultDO> findByTaskId(@Param("taskId") Long taskId);


    /**
     * query all task result
     * @return all task result list
     */
    List<HokageTaskResultDO> listAll();
}
