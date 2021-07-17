package com.hokage.biz.service.impl;

import com.hokage.biz.converter.server.ServerGroupConverter;
import com.hokage.biz.enums.RecordStatusEnum;
import com.hokage.biz.enums.ResultCodeEnum;
import com.hokage.biz.enums.SequenceNameEnum;
import com.hokage.biz.form.server.ServerOperateForm;
import com.hokage.biz.response.HokageOptionVO;
import com.hokage.biz.service.HokageSequenceService;
import com.hokage.biz.service.HokageServerGroupService;
import com.hokage.common.ServiceResponse;
import com.hokage.persistence.dao.HokageServerGroupDao;
import com.hokage.persistence.dataobject.HokageServerGroupDO;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.hokage.biz.form.server.ServerOperateForm.ServerGroupForm;

/**
 * @author linyimin
 * @date 2021/2/27 18:16 pm
 * @email linyimin520812@gmail.com
 * @description server group service implementation
 */
@Service
public class HokageServerGroupServiceImpl extends HokageServiceResponse implements HokageServerGroupService {

    private HokageServerGroupDao serverGroupDao;
    private HokageSequenceService sequenceService;
    private ServerGroupConverter groupConverter;

    @Autowired
    public void setServerGroupDao(HokageServerGroupDao serverGroupDao) {
        this.serverGroupDao = serverGroupDao;
    }

    @Autowired
    public void setSequenceService(HokageSequenceService sequenceService) {
        this.sequenceService = sequenceService;
    }

    @Autowired
    public void setGroupConverter(ServerGroupConverter groupConverter) {
        this.groupConverter = groupConverter;
    }

    @Override
    public ServiceResponse<Boolean> insert(HokageServerGroupDO serverGroupDO) {
        if (serverGroupDao.insert(serverGroupDO) > 0) {
            return success(true);
        }
        return fail(ResultCodeEnum.SERVER_SYSTEM_ERROR.getCode(), ResultCodeEnum.SERVER_SYSTEM_ERROR.getMsg());
    }

    @Override
    public ServiceResponse<List<HokageOptionVO<String>>> listGroupOptions() {

        List<HokageServerGroupDO> serverGroupDOList = serverGroupDao.selectAll();
        if (CollectionUtils.isEmpty(serverGroupDOList)) {
            return success(Collections.emptyList());
        }
        List<HokageOptionVO<String>> optionVOList = serverGroupDOList
                .stream()
                .map(groupConverter::toOption)
                .collect(Collectors.toList());
        return success(optionVOList);
    }

    @Override
    public ServiceResponse<Boolean> update(HokageServerGroupDO serverGroupDO) {

        if (serverGroupDao.update(serverGroupDO) > 0) {
            return success(true);
        }
        return fail(ResultCodeEnum.SERVER_SYSTEM_ERROR.getCode(), ResultCodeEnum.SERVER_SYSTEM_ERROR.getMsg());
    }

    @Override
    public ServiceResponse<HokageServerGroupDO> upsert(HokageServerGroupDO serverGroupDO) {

        if (ObjectUtils.defaultIfNull(serverGroupDO.getId(), 0L) == 0) {
            ServiceResponse<Long> sequenceResult = sequenceService.nextValue(SequenceNameEnum.hokage_server_group.name());
            if (!sequenceResult.getSucceeded()) {
                throw new RuntimeException("server group upsert error. reason: " + sequenceResult.getMsg());
            }
            serverGroupDO.setId(sequenceResult.getData());
            Long result = serverGroupDao.insert(serverGroupDO);
            if (result > 0) {
                return success(serverGroupDO);
            }
            throw new RuntimeException("server group upsert error.");
        }

        Long result = serverGroupDao.update(serverGroupDO);
        if (result > 0) {
            return success(serverGroupDO);
        }
        throw new RuntimeException("server group upsert error.");
    }

    @Override
    public ServiceResponse<List<HokageServerGroupDO>> listByCreatorId(Long id) {
        if (ObjectUtils.defaultIfNull(id, 0L) == 0) {
            return success(Collections.emptyList());
        }
        List<HokageServerGroupDO> serverGroupDOList = serverGroupDao.listByCreatorId(id);

        if (CollectionUtils.isEmpty(serverGroupDOList)) {
            return success(Collections.emptyList());
        }
        return success(serverGroupDOList);
    }

    @Override
    public ServiceResponse<List<HokageOptionVO<String>>> listGroup(Long id) {
        List<HokageServerGroupDO> serverGroupDOList = serverGroupDao.listByCreatorId(id);
        if (CollectionUtils.isEmpty(serverGroupDOList)) {
            return success(Collections.emptyList());
        }
        List<HokageOptionVO<String>> optionVOList = serverGroupDOList.stream()
                .map(serverGroupDO -> new HokageOptionVO<>(serverGroupDO.getName(), serverGroupDO.getName()))
                .collect(Collectors.toList());
        return success(optionVOList);
    }

    @Override
    public ServiceResponse<Boolean> addGroup(HokageServerGroupDO groupDO) {

        List<HokageServerGroupDO> serverGroupDOList = serverGroupDao.selectAll();

        // group name has existed, return directly
        boolean hasExisted = serverGroupDOList.stream().anyMatch(serverGroupDO -> StringUtils.equals(groupDO.getName(), serverGroupDO.getName()));
        if (hasExisted) {
            return fail(ResultCodeEnum.SERVER_GROUP_EXISTED.getCode(), ResultCodeEnum.SERVER_GROUP_EXISTED.getMsg());
        }

        ServiceResponse<Long> sequenceResult = sequenceService.nextValue(SequenceNameEnum.hokage_server_group.name());
        if (!sequenceResult.getSucceeded()) {
            throw new RuntimeException("adding group get sequence key error. reason: " + sequenceResult.getMsg());
        }
        groupDO.setId(sequenceResult.getData());
        groupDO.setStatus(RecordStatusEnum.inuse.getStatus());
        return success(serverGroupDao.insert(groupDO) > 0);
    }
}
