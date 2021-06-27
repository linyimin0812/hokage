package com.hokage.biz.service.impl;

import com.hokage.biz.enums.ResultCodeEnum;
import com.hokage.biz.enums.SequenceNameEnum;
import com.hokage.biz.response.bat.TaskResultDetailVO;
import com.hokage.biz.service.HokageSequenceService;
import com.hokage.biz.service.HokageTaskResultService;
import com.hokage.common.ServiceResponse;
import com.hokage.persistence.dao.HokageTaskResultDao;
import com.hokage.persistence.dataobject.HokageFixedDateTaskDO;
import com.hokage.persistence.dataobject.HokageTaskResultDO;
import com.hokage.ssh.enums.TaskStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author yiminlin
 * @date 2021/06/28 12:07 am
 * @description task result service interface implementtation
 **/
@Service
public class HokageTaskResultServiceImpl implements HokageTaskResultService {
    private HokageTaskResultDao taskResultDao;
    private HokageSequenceService sequenceService;

    @Autowired
    public void setTaskResultDao(HokageTaskResultDao taskResultDao) {
        this.taskResultDao = taskResultDao;
    }

    @Autowired
    public void setSequenceService(HokageSequenceService sequenceService) {
        this.sequenceService = sequenceService;
    }

    @Override
    public ServiceResponse<Long> upsert(HokageTaskResultDO taskResultDO) {
        ServiceResponse<Long> response = new ServiceResponse<>();
        if (Objects.isNull(taskResultDO.getId()) || taskResultDO.getId() == 0) {
            ServiceResponse<Long> sequenceResponse = sequenceService.nextValue(SequenceNameEnum.hokage_task_result.name());
            taskResultDO.setId(sequenceResponse.getData());
            Long result = taskResultDao.insert(taskResultDO);
            return result > 0 ? response.success(taskResultDO.getId()) : response.fail(ResultCodeEnum.SERVER_SYSTEM_ERROR.getCode(), "insert task result error.");
        }
        Long result = taskResultDao.update(taskResultDO);
        return result > 0 ? response.success(taskResultDO.getId()) : response.fail(ResultCodeEnum.SERVER_SYSTEM_ERROR.getCode(), "update task result error.");
    }

    @Override
    public ServiceResponse<HokageFixedDateTaskDO> findById(Long id) {
        ServiceResponse<HokageFixedDateTaskDO> response = new ServiceResponse<>();
        HokageTaskResultDO taskDO = taskResultDao.findById(id);
        if (Objects.isNull(taskDO)) {
            return response.fail(ResultCodeEnum.SERVER_SYSTEM_ERROR.getCode(), "task result is not exit. id: " + id);
        }
        // TODO: 转换
        return response.success(null);
    }

    @Override
    public ServiceResponse<List<TaskResultDetailVO>> findByTaskId(Long taskId) {
        ServiceResponse<List<TaskResultDetailVO>> response = new ServiceResponse<>();
        List<HokageTaskResultDO> list = taskResultDao.findByTaskId(taskId);
        // TODO: 转换
        return response.success(null);
    }

    @Override
    public ServiceResponse<Boolean> delete(Long id) {
        ServiceResponse<Boolean> response = new ServiceResponse<>();
        HokageTaskResultDO taskResultDO = taskResultDao.findById(id);
        if (Objects.isNull(taskResultDO)) {
            return response.fail(ResultCodeEnum.TASK_RESULT_NOT_FOUND.getCode(), ResultCodeEnum.TASK_RESULT_NOT_FOUND.getMsg());
        }
        taskResultDO.setStatus(TaskStatusEnum.delete.getStatus());
        Long result = taskResultDao.update(taskResultDO);
        return result > 0 ? response.success(Boolean.TRUE) : response.fail(ResultCodeEnum.SERVER_SYSTEM_ERROR.getCode(), "delete task result failed");
    }
}
