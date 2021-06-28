package com.hokage.biz.service.impl;

import com.hokage.biz.converter.bat.TaskResultDetailConverter;
import com.hokage.biz.enums.ResultCodeEnum;
import com.hokage.biz.enums.SequenceNameEnum;
import com.hokage.biz.response.bat.TaskResultDetailVO;
import com.hokage.biz.response.bat.TaskResultVO;
import com.hokage.biz.service.HokageSequenceService;
import com.hokage.biz.service.HokageTaskResultService;
import com.hokage.common.ServiceResponse;
import com.hokage.persistence.dao.HokageTaskResultDao;
import com.hokage.persistence.dataobject.HokageTaskResultDO;
import com.hokage.ssh.enums.TaskStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author yiminlin
 * @date 2021/06/28 12:07 am
 * @description task result service interface implementtation
 **/
@Service
public class HokageTaskResultServiceImpl implements HokageTaskResultService {
    private HokageTaskResultDao taskResultDao;
    private HokageSequenceService sequenceService;
    private TaskResultDetailConverter detailConverter;

    @Autowired
    public void setTaskResultDao(HokageTaskResultDao taskResultDao) {
        this.taskResultDao = taskResultDao;
    }

    @Autowired
    public void setSequenceService(HokageSequenceService sequenceService) {
        this.sequenceService = sequenceService;
    }

    @Autowired
    public void setDetailConverter(TaskResultDetailConverter detailConverter) {
        this.detailConverter = detailConverter;
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
    public ServiceResponse<TaskResultVO> findById(Long id) {
        ServiceResponse<TaskResultVO> response = new ServiceResponse<>();
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
        List<TaskResultDetailVO> list = taskResultDao.findByTaskId(taskId).stream().map(detailConverter::doForward).collect(Collectors.toList());
        return response.success(list);
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

    @Override
    public ServiceResponse<List<TaskResultVO>> listByUserId(Long userId) {
        ServiceResponse<List<TaskResultVO>> response = new ServiceResponse<>();
        List<HokageTaskResultDO> resultDOList = taskResultDao.listByUserId(userId);
        Map<Long, List<HokageTaskResultDO>> taskResultMap = resultDOList.stream().collect(Collectors.groupingBy(HokageTaskResultDO::getTaskId));
        List<TaskResultVO> resultVOList = taskResultMap.values().stream().map(this::taskResultList2VO).collect(Collectors.toList());
        return response.success(resultVOList);
    }

    private TaskResultVO taskResultList2VO(List<HokageTaskResultDO> taskResultDOList) {
        TaskResultVO resultVO = new TaskResultVO();
        // TODO:
        return resultVO;
    }
}
