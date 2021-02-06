package com.banzhe.hokage.biz.service.impl;

import com.banzhe.hokage.biz.converter.server.ConverterTypeEnum;
import com.banzhe.hokage.biz.converter.server.ServerDOConverter;
import com.banzhe.hokage.biz.converter.server.ServerFormConverter;
import com.banzhe.hokage.biz.converter.server.ServerSearchFormConverter;
import com.banzhe.hokage.biz.enums.SequenceNameEnum;
import com.banzhe.hokage.biz.enums.ErrorCodeEnum;
import com.banzhe.hokage.biz.enums.UserRoleEnum;
import com.banzhe.hokage.biz.form.server.HokageServerForm;
import com.banzhe.hokage.biz.form.server.ServerOperateForm;
import com.banzhe.hokage.biz.form.server.ServerSearchForm;
import com.banzhe.hokage.biz.request.AllServerQuery;
import com.banzhe.hokage.biz.request.SubordinateServerQuery;
import com.banzhe.hokage.biz.request.SupervisorServerQuery;
import com.banzhe.hokage.biz.response.server.HokageServerVO;
import com.banzhe.hokage.biz.service.HokageSequenceService;
import com.banzhe.hokage.biz.service.HokageServerService;
import com.banzhe.hokage.biz.service.HokageUserService;
import com.banzhe.hokage.common.ServiceResponse;
import com.banzhe.hokage.persistence.dao.HokageServerDao;
import com.banzhe.hokage.persistence.dao.HokageSupervisorServerDao;
import com.banzhe.hokage.persistence.dataobject.HokageServerDO;
import com.banzhe.hokage.persistence.dataobject.HokageSupervisorServerDO;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * @author linyimin
 * @date 2020/10/31 1:20 am
 * @email linyimin520812@gmail.com
 * @description hokage server service implementation
 */
@Service
public class HokageServerServiceImpl implements HokageServerService {

    private HokageServerDao hokageServerDao;
    private HokageUserService userService;
    private ServerFormConverter formConverter;
    private HokageSequenceService sequenceService;
    private HokageSupervisorServerDao supervisorServerDao;

    @Autowired
    public void setHokageServerDao(HokageServerDao hokageServerDao) {
        this.hokageServerDao = hokageServerDao;
    }

    @Autowired
    public void setUserService(HokageUserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setFormConverter(ServerFormConverter converter) {
        this.formConverter = converter;
    }

    @Autowired
    private void setSupervisorServerDao(HokageSupervisorServerDao supervisorServerDao) {
        this.supervisorServerDao = supervisorServerDao;
    }

    @Autowired
    private void setSequenceService(HokageSequenceService service) {
        this.sequenceService = service;
    }

    private final ImmutableMap<Integer, Function<ServerSearchForm, List<HokageServerDO>>> SERVER_QUERY_MAP =
            ImmutableMap.<Integer, Function<ServerSearchForm, List<HokageServerDO>>>builder()
                    .put(UserRoleEnum.super_operator.getValue(), (form -> {
                        AllServerQuery query = ServerSearchFormConverter.converterToAll(form);
                        return hokageServerDao.selectByQuery(query);

                    }))
                    .put(UserRoleEnum.supervisor.getValue(), (form -> {
                        SupervisorServerQuery query = ServerSearchFormConverter.converterToSupervisor(form);
                        return hokageServerDao.selectByQuery(query);
                    }))
                    .put(UserRoleEnum.subordinate.getValue(), (form -> {
                        SubordinateServerQuery query = ServerSearchFormConverter.converterToSubordinate(form);
                        return hokageServerDao.selectByQuery(query);
                    }))
                    .build();

    @Override
    public ServiceResponse<List<HokageServerVO>> selectAll() {
        ServiceResponse<List<HokageServerVO>> response = new ServiceResponse<>();
        List<HokageServerDO> serverDOList = hokageServerDao.selectAll();
        List<HokageServerVO> serverVOList = serverDOList.stream()
                .map(serverDO -> ServerDOConverter.converterDO2VO(serverDO, ConverterTypeEnum.all))
                .collect(Collectors.toList());
        response.success(serverVOList);
        return response;
    }

    @Override
    public ServiceResponse<List<HokageServerVO>> listServer(ServerSearchForm form) {

        ServiceResponse<List<HokageServerVO>> response = new ServiceResponse<>();

        Long operateId = form.getOperateId();
        // retrieve operate role
        ServiceResponse<Integer> roleResponse = userService.getRoleByUserId(operateId);

        if (!roleResponse.getSucceeded() || Objects.isNull(roleResponse.getData())) {
            return response.fail(roleResponse.getCode(), roleResponse.getMsg());
        }

        Function<ServerSearchForm, List<HokageServerDO>> queryFunction = SERVER_QUERY_MAP.get(roleResponse.getData());

        if (Objects.isNull(queryFunction)) {
            return response.fail("A-XXX", "search server error, not support search type.");
        }
        List<HokageServerDO> serverDOList = queryFunction.apply(form);

        List<HokageServerVO> serverVOList = serverDOList.stream()
                .map(serverDO -> ServerDOConverter.converterDO2VO(serverDO, ConverterTypeEnum.all))
                .collect(Collectors.toList());

        response.success(serverVOList);

        return response;

    }

    @Override
    public ServiceResponse<List<HokageServerDO>> selectByIds(List<Long> ids) {

        ServiceResponse<List<HokageServerVO>> response = new ServiceResponse<>();

        List<HokageServerDO> serverDOList = hokageServerDao.selectByIds(ids);

        if (CollectionUtils.isEmpty(serverDOList)) {
            response.success(Collections.emptyList());
        }

        return null;
    }

    @Override
    public ServiceResponse<List<HokageServerDO>> selectByType(String type) {
        return null;
    }

    @Override
    public ServiceResponse<List<HokageServerDO>> selectByGroup(String group) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServiceResponse<HokageServerForm> save(HokageServerForm form) {

        ServiceResponse<HokageServerForm> response = new ServiceResponse<>();

        HokageServerDO serverDO = formConverter.doForward(form);

        // TODO: get hostname from ssh

        if (Objects.isNull(form.getId()) || form.getId()  == 0) {
            // insert
            ServiceResponse<Long> primaryKeyResponse = sequenceService.nextValue(SequenceNameEnum.hokage_server.name());
            if (!primaryKeyResponse.getSucceeded() || primaryKeyResponse.getData() <= 0) {
                throw new RuntimeException("sequenceService get hokage_server primary key value error.");
            }
            serverDO.setId(primaryKeyResponse.getData());

            if (hokageServerDao.insert(serverDO) <= 0) {
                throw new RuntimeException("ServerService insert server info error.");
            }

        } else {
            // update
            if (hokageServerDao.update(serverDO) <= 0) {
                throw new RuntimeException("ServerService update server info error.");
            }
        }

        // add supervisor
        if (!CollectionUtils.isEmpty(form.getSupervisors())) {
            List<HokageSupervisorServerDO> supervisorServerDOS = supervisorServerDao.listByServerIds(Collections.singletonList(serverDO.getId()));

            List<Long> supervisorIds = form.getSupervisors();

            List<Long> addList = new ArrayList<>();
            List<Long> deleteList = new ArrayList<>();

            if (!CollectionUtils.isEmpty(supervisorServerDOS)) {
                addList = supervisorIds.stream().filter(id -> supervisorServerDOS.stream()
                        .noneMatch(superServerDO -> id.equals(superServerDO.getSupervisorId())))
                        .collect(Collectors.toList());

                deleteList = supervisorServerDOS.stream()
                        .map(HokageSupervisorServerDO::getSupervisorId)
                        .filter(id -> !supervisorIds.contains(id))
                        .collect(Collectors.toList());
            } else {
                addList.addAll(supervisorIds);
            }

            boolean result = addList.stream().allMatch(id -> {
                HokageSupervisorServerDO supervisorServerDO = new HokageSupervisorServerDO();
                ServiceResponse<Long> primaryKeyRes = sequenceService.nextValue(SequenceNameEnum.hokage_supervisor_server.name());
                supervisorServerDO.setId(primaryKeyRes.getData());
                supervisorServerDO.setServerId(serverDO.getId());
                supervisorServerDO.setSupervisorId(id);
                return supervisorServerDao.insert(supervisorServerDO) > 0;
            });

            if (!CollectionUtils.isEmpty(deleteList)) {
                result &= deleteList.stream().allMatch(id -> supervisorServerDao.removeBySupervisorId(id, Collections.singletonList(serverDO.getId())) > 0);
            }

            if (!result) {
                throw new RuntimeException("update supervisor error.");
            }
        }
        form.setId(serverDO.getId());

        return response.success(form);
    }

    @Override
    public ServiceResponse<Boolean> delete(ServerOperateForm form) {
        // TODO: 区分实现，参考listServer的实现
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServiceResponse<Boolean> designateSupervisor(ServerOperateForm form) {

        Long operatorId = checkNotNull(form.getId(), "id can't be null");
        checkState(!CollectionUtils.isEmpty(form.getUserIds()), "user ids can't be null");
        checkState(!CollectionUtils.isEmpty(form.getServerIds()), "server ids can't be null");

        ServiceResponse<Boolean> response = new ServiceResponse<>();

        List<Long> supervisorIds = form.getUserIds();
        List<Long> serverIds = form.getServerIds();

                // retrieve operate role
        ServiceResponse<Integer> roleResponse = userService.getRoleByUserId(operatorId);

        if (!roleResponse.getSucceeded() || Objects.isNull(roleResponse.getData())) {
            return response.fail(roleResponse.getCode(), roleResponse.getMsg());
        }

        if (!UserRoleEnum.super_operator.getValue().equals(roleResponse.getData())) {
            return response.fail(ErrorCodeEnum.USER_NO_PERMISSION.getCode(), ErrorCodeEnum.USER_NO_PERMISSION.getMsg());
        }

        boolean result = supervisorIds.stream().anyMatch(supervisorId -> serverIds.stream().anyMatch(serverId -> {
            HokageSupervisorServerDO supervisorServerDO = supervisorServerDao.queryBySupervisorIdAndServerId(supervisorId, serverId);
            if (Objects.nonNull(supervisorServerDO)) {
                return true;
            }

            ServiceResponse<Long> primaryKeyRes = sequenceService.nextValue(SequenceNameEnum.hokage_supervisor_server.name());
            supervisorServerDO = new HokageSupervisorServerDO();
            supervisorServerDO.setId(primaryKeyRes.getData());
            supervisorServerDO.setSupervisorId(supervisorId);
            supervisorServerDO.setServerId(serverId);

            return supervisorServerDao.insert(supervisorServerDO) > 0;
        }));

        if (result) {
            return response.success(true);
        }

        return response.fail(ErrorCodeEnum.SERVER_SYSTEM_ERROR.getCode(), ErrorCodeEnum.SERVER_SYSTEM_ERROR.getMsg());
    }
}
