package com.hokage.biz.service.impl;

import com.hokage.biz.converter.bat.TaskResultDetailConverter;
import com.hokage.biz.enums.ResultCodeEnum;
import com.hokage.biz.enums.SequenceNameEnum;
import com.hokage.biz.enums.bat.TaskResultStatusEnum;
import com.hokage.biz.enums.bat.TriggerTypeEnum;
import com.hokage.biz.response.bat.TaskResultDetailVO;
import com.hokage.biz.response.bat.TaskResultVO;
import com.hokage.biz.service.HokageSequenceService;
import com.hokage.biz.service.HokageServerService;
import com.hokage.biz.service.HokageTaskResultService;
import com.hokage.common.ServiceResponse;
import com.hokage.infra.worker.BatCommandScheduledPoolWorker;
import com.hokage.infra.worker.BatCommandThreadPoolWorker;
import com.hokage.persistence.dao.HokageFixedDateTaskDao;
import com.hokage.persistence.dao.HokageTaskResultDao;
import com.hokage.persistence.dataobject.HokageFixedDateTaskDO;
import com.hokage.persistence.dataobject.HokageServerDO;
import com.hokage.persistence.dataobject.HokageTaskResultDO;
import com.hokage.biz.enums.bat.TaskStatusEnum;
import com.hokage.ssh.SshClient;
import com.hokage.ssh.command.result.CommandResult;
import com.hokage.ssh.component.SshExecComponent;
import com.hokage.ssh.context.SshContext;
import com.hokage.util.TimeUtil;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author yiminlin
 * @date 2021/06/28 12:07 am
 * @description task result service interface implementtation
 **/
@Slf4j
@Service
public class HokageTaskResultServiceImpl implements HokageTaskResultService {
    private HokageTaskResultDao taskResultDao;
    private HokageSequenceService sequenceService;
    private TaskResultDetailConverter detailConverter;
    private HokageFixedDateTaskDao fixedDateTaskDao;
    private BatCommandThreadPoolWorker threadPoolWorker;
    private BatCommandScheduledPoolWorker scheduledPoolWorker;
    private HokageServerService serverService;
    private SshExecComponent execComponent;

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

    @Autowired
    public void setFixedDateTaskDao(HokageFixedDateTaskDao fixedDateTaskDao) {
        this.fixedDateTaskDao = fixedDateTaskDao;
    }

    @Autowired
    public void setServerService(HokageServerService serverService) {
        this.serverService = serverService;
    }

    @Autowired
    public void setScheduledPoolWorker(BatCommandScheduledPoolWorker scheduledPoolWorker) {
        this.scheduledPoolWorker = scheduledPoolWorker;
    }

    @Autowired
    public void setThreadPoolWorker(BatCommandThreadPoolWorker threadPoolWorker) {
        this.threadPoolWorker = threadPoolWorker;
    }

    @Autowired
    public void setExecComponent(SshExecComponent execComponent) {
        this.execComponent = execComponent;
    }

    @PostConstruct
    public void init() {
        scheduledPoolWorker.getScheduledService().scheduleAtFixedRate(() -> {
            try {
                long start = System.currentTimeMillis();
                long end = start + 5 * 60 * 1000;
                List<HokageFixedDateTaskDO> taskDOList = fixedDateTaskDao.listRunnableTask(start, end);
                if (CollectionUtils.isEmpty(taskDOList)) {
                    return;
                }
                taskDOList.forEach(taskDO -> this.execute(taskDO, TriggerTypeEnum.auto_scheduled));
            } catch (Exception e) {
                log.error("bat command scheduled error, errMsg: " + e.getMessage());
            }
        }, 0, 60, TimeUnit.SECONDS);
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
        List<TaskResultVO> resultVOList = taskResultMap.values().stream().map(list -> {
            Map<String, List<HokageTaskResultDO>> batchResultMap = list.stream().collect(Collectors.groupingBy(HokageTaskResultDO::getBatchId));
            return batchResultMap.values().stream().map(this::taskResultList2VO).collect(Collectors.toList());
        }).flatMap(Collection::stream).sorted(Comparator.comparing(TaskResultVO::getStartTime).reversed()).collect(Collectors.toList());
        return response.success(resultVOList);
    }

    @Override
    public void execute(Long id, TriggerTypeEnum triggerType) {
        HokageFixedDateTaskDO taskDO = fixedDateTaskDao.findById(id);
        if (Objects.isNull(taskDO)) {
            throw new RuntimeException("task is not exist, task id: " + id);
        }
        this.execute(taskDO, TriggerTypeEnum.manual);
    }

    @Override
    public void execute(HokageFixedDateTaskDO taskDO, TriggerTypeEnum triggerType) {
        List<Long> execServerIdList = Arrays.stream(StringUtils.split(taskDO.getExecServers(), ","))
                .map(Long::parseLong).collect(Collectors.toList());
        ServiceResponse<List<HokageServerDO>> serverResponse = serverService.selectByIds(execServerIdList);
        if (!serverResponse.getSucceeded() || CollectionUtils.isEmpty(serverResponse.getData())) {
            throw new RuntimeException("execute server list is empty. task id: " + taskDO.getId());
        }

        String uuid = UUID.randomUUID().toString();
        serverResponse.getData().forEach(server -> {
            HokageTaskResultDO taskResultDO = this.preExecuteCommand(taskDO, triggerType, server.getId(), uuid);
            threadPoolWorker.getExecutorPool().execute(() -> this.executeCommand(server, taskResultDO, taskDO.getExecCommand()));
        });
    }

    private TaskResultVO taskResultList2VO(List<HokageTaskResultDO> taskResultDOList) {
        if (CollectionUtils.isEmpty(taskResultDOList)) {
            return  null;
        }
        TaskResultVO resultVO = new TaskResultVO();

        HokageTaskResultDO resultDO = taskResultDOList.get(0);
        resultVO.setTaskId(resultDO.getTaskId());
        resultVO.setTriggerType(resultDO.getTriggerType());
        resultVO.setBatchId(resultDO.getBatchId());
        resultVO.setExitCode(resultDO.getExitCode());

        HokageFixedDateTaskDO taskDO = fixedDateTaskDao.findById(resultDO.getTaskId());
        if (Objects.isNull(taskDO)) {
            throw new RuntimeException("task is not exit, task id: " + resultDO.getTaskId());
        }
        resultVO.setTaskName(taskDO.getTaskName());

        long startTime = taskResultDOList.stream().map(HokageTaskResultDO::getStartTime).min(Long::compareTo).orElse(0L);
        resultVO.setStartTime(TimeUtil.format(startTime, TimeUtil.DISPLAY_FORMAT));

        long endTime = taskResultDOList.stream().map(result -> Optional.ofNullable(result.getEndTime()).orElse(Long.MAX_VALUE)).max(Long::compareTo).orElse(Long.MAX_VALUE);
        if (endTime < Long.MAX_VALUE) {
            resultVO.setEndTime(TimeUtil.format(endTime, TimeUtil.DISPLAY_FORMAT));
            resultVO.setCost(endTime - startTime);
            resultVO.setTaskStatus(TaskResultStatusEnum.finished.getStatus());
        } else {
            resultVO.setTaskStatus(TaskResultStatusEnum.running.getStatus());
        }

        boolean isAllSucceed = taskResultDOList.stream().allMatch(taskResultDO -> TaskResultStatusEnum.finished.getStatus().equals(taskResultDO.getTaskStatus()));
        if (!isAllSucceed) {
            resultVO.setTaskStatus(TaskResultStatusEnum.failed.getStatus());
        }

        List<TaskResultDetailVO> list = taskResultDao.listByBatchId(resultDO.getBatchId())
                .stream()
                .map(detailConverter::doForward)
                .collect(Collectors.toList());

        resultVO.setResultDetailVOList(list);

        return resultVO;
    }

    private HokageTaskResultDO preExecuteCommand(HokageFixedDateTaskDO taskDO, TriggerTypeEnum triggerType, Long serverId, String batchId) {
        HokageTaskResultDO resultDO = new HokageTaskResultDO();
        long start = System.currentTimeMillis();
        resultDO.setStartTime(start).setTaskId(taskDO.getId())
                .setTaskStatus(TaskResultStatusEnum.running.getStatus())
                .setTriggerType(triggerType.getStatus())
                .setExecServer(serverId)
                .setUserId(taskDO.getUserId())
                .setStatus(taskDO.getStatus())
                .setBatchId(batchId);

        ServiceResponse<Long> upsert = this.upsert(resultDO);
        if (!upsert.getSucceeded()) {
            throw new RuntimeException(String.format("upsert task result error. code: %s, msg: %s", upsert.getCode(), upsert.getMsg()));
        }
        return resultDO;
    }

    private void executeCommand(HokageServerDO serverDO, HokageTaskResultDO taskResultDO, String command) {
        SshClient client = null;
        try {
            SshContext context = new SshContext();
            BeanUtils.copyProperties(serverDO, context);
            client = new SshClient(context);
            CommandResult commandResult = execComponent.execute(client, command);
            if (commandResult.isSuccess()) {
                taskResultDO.setTaskStatus(TaskResultStatusEnum.finished.getStatus()).setExecResult(commandResult.getContent());
            } else {
                taskResultDO.setTaskStatus(TaskResultStatusEnum.failed.getStatus()).setExecResult(commandResult.getMsg());
            }
            taskResultDO.setEndTime(System.currentTimeMillis()).setExitCode(commandResult.getExitStatus());
        } catch (Exception e) {
            taskResultDO.setEndTime(System.currentTimeMillis()).setTaskStatus(TaskResultStatusEnum.failed.getStatus()).setExecResult(e.getMessage());
        } finally {
            this.upsert(taskResultDO);
            if (Objects.nonNull(client)) {
                Session session =  client.getSessionIfPresent();
                if (Objects.nonNull(session)) {
                    session.disconnect();
                }
            }
        }
    }
}
