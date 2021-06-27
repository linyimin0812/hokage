package com.hokage.biz.service.impl;

import com.hokage.biz.enums.ResultCodeEnum;
import com.hokage.biz.enums.SequenceNameEnum;
import com.hokage.biz.response.bat.HokageFixedDateTaskVO;
import com.hokage.biz.service.HokageFixedDateTaskService;
import com.hokage.biz.service.HokageSequenceService;
import com.hokage.biz.service.HokageServerService;
import com.hokage.common.ServiceResponse;
import com.hokage.persistence.dao.HokageFixedDateTaskDao;
import com.hokage.persistence.dataobject.HokageFixedDateTaskDO;
import com.hokage.persistence.dataobject.HokageServerDO;
import com.hokage.util.TimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author yiminlin
 * @date 2021/06/27 4:37 am
 * @description fixed date task service implementation
 **/
@Service
public class HokageFixedDateTaskServiceImpl implements HokageFixedDateTaskService {

    private HokageFixedDateTaskDao fixedDateTaskDao;
    private HokageSequenceService sequenceService;
    private HokageServerService serverService;

    @Autowired
    public void setFixedDateTaskDao(HokageFixedDateTaskDao fixedDateTaskDao) {
        this.fixedDateTaskDao = fixedDateTaskDao;
    }

    @Autowired
    public void setSequenceService(HokageSequenceService sequenceService) {
        this.sequenceService = sequenceService;
    }

    @Autowired
    public void setServerService(HokageServerService serverService) {
        this.serverService = serverService;
    }

    @Override
    public ServiceResponse<Long> upsert(HokageFixedDateTaskDO fixedDateTaskDO) {
        ServiceResponse<Long> response = new ServiceResponse<>();
        if (Objects.isNull(fixedDateTaskDO.getId()) || fixedDateTaskDO.getId() == 0) {
            ServiceResponse<Long> sequenceResult = sequenceService.nextValue(SequenceNameEnum.hokage_fixed_date_task.name());
            fixedDateTaskDO.setId(sequenceResult.getData());
            Long result = fixedDateTaskDao.insert(fixedDateTaskDO);
            return result > 0 ? response.success(sequenceResult.getData()) : response.fail(ResultCodeEnum.SERVER_SYSTEM_ERROR.getCode(), "insert fixed date error");
        }
        Long result = fixedDateTaskDao.update(fixedDateTaskDO);
        return result > 0 ? response.success(result) : response.fail(ResultCodeEnum.SERVER_SYSTEM_ERROR.getCode(), "update fixed date error");
    }

    @Override
    public ServiceResponse<HokageFixedDateTaskDO> findById(Long id) {
        ServiceResponse<HokageFixedDateTaskDO> response = new ServiceResponse<>();
        return response.success(fixedDateTaskDao.findById(id));
    }

    @Override
    public ServiceResponse<List<HokageFixedDateTaskDO>> findByName(String name) {
        ServiceResponse<List<HokageFixedDateTaskDO>> response = new ServiceResponse<>();

        return response.success(fixedDateTaskDao.findByName(name));
    }

    @Override
    public ServiceResponse<List<HokageFixedDateTaskDO>> findByType(Integer type) {
        ServiceResponse<List<HokageFixedDateTaskDO>> response = new ServiceResponse<>();

        return response.success(fixedDateTaskDao.findByType(type));
    }

    @Override
    public ServiceResponse<List<HokageFixedDateTaskDO>> findAll(HokageFixedDateTaskDO fixedDateTaskDO) {
        ServiceResponse<List<HokageFixedDateTaskDO>> response = new ServiceResponse<>();

        return response.success(fixedDateTaskDao.findAll(fixedDateTaskDO));
    }

    @Override
    public ServiceResponse<List<HokageFixedDateTaskVO>> listByUserId(Long userId) {
        ServiceResponse<List<HokageFixedDateTaskVO>> response = new ServiceResponse<>();
        List<HokageFixedDateTaskDO> taskDOList = fixedDateTaskDao.listByUserId(userId);

        List<HokageFixedDateTaskVO> taskVOList = taskDOList.stream().map(taskDO -> {
            HokageFixedDateTaskVO taskVO = new HokageFixedDateTaskVO();
            BeanUtils.copyProperties(taskDO, taskVO);

            List<Long> serverIds = Arrays.stream(StringUtils.split(taskDO.getExecServers(), ",")).map(Long::parseLong).collect(Collectors.toList());
            ServiceResponse<List<HokageServerDO>> listServiceResponse = serverService.selectByIds(serverIds);

            List<Long> serverIdList = new ArrayList<>();
            List<String> serverIpList = new ArrayList<>();
            listServiceResponse.getData().forEach(serverDO -> {
                serverIdList.add(serverDO.getId());
                serverIpList.add(serverDO.getIp());
            });
            taskVO.setExecServers(serverIdList);
            taskVO.setExecServerList(serverIpList);

            taskVO.setExecType(0);

            taskVO.setExecTime(TimeUtil.format(taskDO.getExecTime(), "yyyy-MM-dd HH:mm"));

            return taskVO;
        }).collect(Collectors.toList());

        return response.success(taskVOList);
    }
}
