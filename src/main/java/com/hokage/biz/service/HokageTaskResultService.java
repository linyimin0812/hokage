package com.hokage.biz.service;

import com.hokage.biz.enums.bat.TriggerTypeEnum;
import com.hokage.biz.response.bat.TaskResultDetailVO;
import com.hokage.biz.response.bat.TaskResultVO;
import com.hokage.common.ServiceResponse;
import com.hokage.persistence.dataobject.HokageFixedDateTaskDO;
import com.hokage.persistence.dataobject.HokageTaskResultDO;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/7/27 11:33 pm
 * @email linyimin520812@gmail.com
 * @description
 */
public interface HokageTaskResultService {
    /**
     * insert or a new task result
     * @param taskResultDO task result DO
     * @return rows affected
     */
    ServiceResponse<Long> upsert(HokageTaskResultDO taskResultDO);

    /**
     * query task result based on task result primary id
     * @param id task result primary id
     * @return task result which id is equal to parameter id
     */
    ServiceResponse<TaskResultVO> findById(Long id);

    /**
     * query task result by task id
     * @param taskId task primary id
     * @return task result list which meet the criteria
     */
    ServiceResponse<List<TaskResultDetailVO>> findByTaskId(Long taskId);

    /**
     * delete task result based on primary id
     * @param id task result primary id
     * @return rows affected
     */
    ServiceResponse<Boolean> delete(Long id);

    /**
     * query task result by user id
     * @param userId user id
     * @return task result list which meet the criteria
     */
    ServiceResponse<List<TaskResultVO>> listByUserId(Long userId);

    /**
     * execute bat command
     * @param id task primary id
     * @param triggerType trigger type, see TriggerTypeEnum
     */
    void execute(Long id, TriggerTypeEnum triggerType);

    /**
     * execute bat command
     * @param taskDO task DO
     * @param triggerType trigger type, see TriggerTypeEnum
     */
    void execute(HokageFixedDateTaskDO taskDO, TriggerTypeEnum triggerType);
}
