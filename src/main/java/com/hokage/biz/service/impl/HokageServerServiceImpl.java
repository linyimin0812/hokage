package com.hokage.biz.service.impl;

import com.alibaba.fastjson.JSON;
import com.hokage.biz.converter.server.ConverterTypeEnum;
import com.hokage.biz.converter.server.ServerDOConverter;
import com.hokage.biz.converter.server.ServerFormConverter;
import com.hokage.biz.enums.LoginTypeEnum;
import com.hokage.biz.enums.SequenceNameEnum;
import com.hokage.biz.enums.ResultCodeEnum;
import com.hokage.biz.enums.UserRoleEnum;
import com.hokage.biz.form.server.HokageServerForm;
import com.hokage.biz.form.server.ServerOperateForm;
import com.hokage.biz.request.AllServerQuery;
import com.hokage.biz.request.ServerQuery;
import com.hokage.biz.request.SubordinateServerQuery;
import com.hokage.biz.request.SupervisorServerQuery;
import com.hokage.biz.response.server.HokageServerVO;
import com.hokage.biz.service.HokageSequenceService;
import com.hokage.biz.service.HokageServerGroupService;
import com.hokage.biz.service.HokageServerService;
import com.hokage.biz.service.HokageUserService;
import com.hokage.common.ServiceResponse;
import com.hokage.persistence.dao.*;
import com.hokage.persistence.dataobject.*;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.*;


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
    private HokageSubordinateServerDao subordinateServerDao;
    private HokageUserDao userDao;
    private HokageServerApplicationDao applicationDao;
    private HokageServerSshKeyContentDao contentDao;

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
    public void setSupervisorServerDao(HokageSupervisorServerDao supervisorServerDao) {
        this.supervisorServerDao = supervisorServerDao;
    }

    @Autowired
    public void setSequenceService(HokageSequenceService service) {
        this.sequenceService = service;
    }

    @Autowired
    public void setSubordinateServerDao(HokageSubordinateServerDao subordinateServerDao) {
        this.subordinateServerDao = subordinateServerDao;
    }

    @Autowired
    public void setApplicationDao(HokageServerApplicationDao applicationDao) {
        this.applicationDao = applicationDao;
    }

    @Autowired
    public void setUserDao(HokageUserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    private void setContentDao(HokageServerSshKeyContentDao contentDao) {
        this.contentDao = contentDao;
    }

    private final ImmutableMap<String, Function<ServerQuery, List<HokageServerDO>>> SERVER_QUERY_MAP =
            ImmutableMap.<String, Function<ServerQuery, List<HokageServerDO>>>builder()
                    .put(AllServerQuery.class.getSimpleName(), (query -> hokageServerDao.selectByQuery((AllServerQuery) query)))
                    .put(SupervisorServerQuery.class.getSimpleName(), (query -> hokageServerDao.selectByQuery((SupervisorServerQuery) query)))
                    .put(SubordinateServerQuery.class.getSimpleName(), (query -> hokageServerDao.selectByQuery((SubordinateServerQuery) query)))
                    .build();

    private final ImmutableMap<Integer, Function<ServerOperateForm, Boolean>> SERVER_APPLY_MAP =
            ImmutableMap.<Integer, Function<ServerOperateForm, Boolean>>builder()
                    .put(UserRoleEnum.supervisor.getValue(), (form -> {
                        List<Long> serverIds = form.getServerIds();
                        return serverIds.stream().anyMatch(serverId -> {
                            HokageServerApplicationDO applicationDO = new HokageServerApplicationDO();

                            applicationDO.setApplyId(form.getId())
                                    .setServerId(serverId);

                            // get approve id
                            List<HokageUserDO> userDOList = userDao.listUserByRole(UserRoleEnum.super_operator.getValue());
                            if (CollectionUtils.isEmpty(userDOList)) {
                                throw new RuntimeException("super operator is empty.");
                            }
                            String approveIds = userDOList.stream().map(userDo -> String.valueOf(userDo.getId())).collect(Collectors.joining(","));
                            applicationDO.setApproveIds(approveIds);
                            return applicationDao.save(applicationDO);
                        });
                    }))
                    .put(UserRoleEnum.subordinate.getValue(), (form -> form.getServerIds().stream()
                            .anyMatch(serverId -> {
                                HokageServerApplicationDO applicationDO = new HokageServerApplicationDO();

                                applicationDO.setApplyId(form.getId())
                                        .setServerId(serverId);

                                // get approve id
                                List<HokageSupervisorServerDO> supervisorServerDOS = supervisorServerDao.listByServerIds(Collections.singletonList(serverId));
                                if (CollectionUtils.isEmpty(supervisorServerDOS)) {
                                    throw new RuntimeException(String.format("supervisor of server id: %s is empty.", serverId));
                                }

                                String approveIds = supervisorServerDOS.stream().map(serverDO -> String.valueOf(serverDO.getId())).collect(Collectors.joining(","));

                                applicationDO.setApproveIds(approveIds);

                                return applicationDao.save(applicationDO);
                            })
                    )).build();

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
    public ServiceResponse<List<HokageServerVO>> searchServer(ServerQuery query) {

        ServiceResponse<List<HokageServerVO>> response = new ServiceResponse<>();

        Function<ServerQuery, List<HokageServerDO>> queryFunction = SERVER_QUERY_MAP.get(query.getClass().getSimpleName());

        if (Objects.isNull(queryFunction)) {
            return response.fail("A-XXX", "search server error, not support search type.");
        }

        List<HokageServerDO> serverDOList = queryFunction.apply(query);
        List<HokageServerVO> serverVOList = serverDOList.stream()
                .map(serverDO -> ServerDOConverter.converterDO2VO(serverDO, ConverterTypeEnum.all))
                .collect(Collectors.toList());

        return response.success(serverVOList);
    }

    @Override
    public ServiceResponse<List<HokageServerDO>> selectByIds(List<Long> ids) {

        ServiceResponse<List<HokageServerDO>> response = new ServiceResponse<>();

        List<HokageServerDO> serverDOList = hokageServerDao.selectByIds(ids);

        if (CollectionUtils.isEmpty(serverDOList)) {
            response.success(Collections.emptyList());
        }

        response.success(serverDOList);

        return response;
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

        // set ssh key content as password if loginType = 1
        updatePasswordContent(serverDO);

        // TODO: get hostname from ssh


        upsertServer(serverDO);

        // add supervisor
        addSupervisor(serverDO, form);

        form.setId(serverDO.getId());

        return response.success(form);
    }

    /**
     * insert or update server
     * @param serverDO
     */
    private void upsertServer(HokageServerDO serverDO) {
        if (Objects.isNull(serverDO.getId()) || serverDO.getId()  == 0) {
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
    }

    /**
     * add supervisor when add server
     * @param serverDO
     * @param form
     */
    private void addSupervisor(HokageServerDO serverDO, HokageServerForm form) {
        if (!CollectionUtils.isEmpty(form.getSupervisorList())) {
            List<HokageSupervisorServerDO> supervisorServerDOList = supervisorServerDao.listByServerIds(Collections.singletonList(serverDO.getId()));

            List<Long> supervisorIds = form.getSupervisorList();

            List<Long> addList;
            List<Long> deleteList = new ArrayList<>();

            if (!CollectionUtils.isEmpty(supervisorServerDOList)) {
                addList = supervisorIds.stream().filter(id -> supervisorServerDOList.stream()
                        .noneMatch(superServerDO -> id.equals(superServerDO.getSupervisorId())))
                        .collect(Collectors.toList());

                deleteList = supervisorServerDOList.stream()
                        .map(HokageSupervisorServerDO::getSupervisorId)
                        .filter(id -> !supervisorIds.contains(id))
                        .collect(Collectors.toList());
            } else {
                addList = new ArrayList<>(supervisorIds);
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
    }

    /**
     * update password content if login type is 1
     * @param serverDO
     */
    private void updatePasswordContent(HokageServerDO serverDO) {
        if (LoginTypeEnum.key.getValue().equals(serverDO.getLoginType())) {
            HokageServerSshKeyContentDO contentDO = contentDao.listByUid(serverDO.getPasswd());
            if (Objects.isNull(contentDO)) {
                throw new RuntimeException("ssh key file content id empty. uid: " + serverDO.getPasswd());
            }
            serverDO.setPasswd(contentDO.getContent());
        }
    }

    @Override
    public ServiceResponse<Boolean> delete(ServerOperateForm form) {
        ServiceResponse<Boolean> response = new ServiceResponse<>();
        long result = hokageServerDao.deleteById(form.getId());
        if (result > 0) {
            return response.success(Boolean.TRUE);
        }
        return response.fail(ResultCodeEnum.SERVER_SYSTEM_ERROR.getCode(), "更新数据库失败");
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
        ServiceResponse<Boolean> isSuperResponse = userService.isSuperOperator(operatorId);

        if (!isSuperResponse.getSucceeded()) {
            return response.fail(isSuperResponse.getCode(), isSuperResponse.getMsg());
        }

        if (!isSuperResponse.getData()) {
            return response.fail(ResultCodeEnum.USER_NO_PERMISSION.getCode(), ResultCodeEnum.USER_NO_PERMISSION.getMsg());
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

        return response.fail(ResultCodeEnum.SERVER_SYSTEM_ERROR.getCode(), ResultCodeEnum.SERVER_SYSTEM_ERROR.getMsg());
    }

    @Override
    public ServiceResponse<Boolean> revokeSupervisor(ServerOperateForm form) {
        Long operatorId = checkNotNull(form.getId(), "operator id can't null");
        checkState(!CollectionUtils.isEmpty(form.getUserIds()));
        checkState(!CollectionUtils.isEmpty(form.getServerIds()));

        ServiceResponse<Boolean> response = new ServiceResponse<>();

        ServiceResponse<Boolean> isSuperResponse = userService.isSuperOperator(operatorId);

        if (!isSuperResponse.getSucceeded()) {
            return response.fail(isSuperResponse.getCode(), isSuperResponse.getMsg());
        }

        if (!isSuperResponse.getData()) {
            return response.fail(ResultCodeEnum.USER_NO_PERMISSION.getCode(), ResultCodeEnum.USER_NO_PERMISSION.getMsg());
        }

        List<Long> userIds = form.getUserIds();
        List<Long> serverIds = form.getServerIds();

        boolean result = userIds.stream().anyMatch(userId -> supervisorServerDao.removeBySupervisorId(userId, serverIds) > 0);

        return response.success(result);
    }

    @Override
    public ServiceResponse<Boolean> designateSubordinate(ServerOperateForm form) {
        Long operatorId = checkNotNull(form.getId(), "id can't be null");
        checkState(!CollectionUtils.isEmpty(form.getUserIds()), "user ids can't be null");
        checkState(!CollectionUtils.isEmpty(form.getServerIds()), "server ids can't be null");

        ServiceResponse<Boolean> response = new ServiceResponse<>();

        ServiceResponse<Boolean> isSupervisorResponse = userService.isSupervisor(operatorId);

        if (!isSupervisorResponse.getSucceeded()) {
            return response.fail(isSupervisorResponse.getCode(), isSupervisorResponse.getMsg());
        }

        if (!isSupervisorResponse.getData()) {
            return response.fail(ResultCodeEnum.USER_NO_PERMISSION.getCode(), ResultCodeEnum.USER_NO_PERMISSION.getMsg());
        }

        List<Long> subordinateIds = form.getUserIds();
        List<Long> serverIds = form.getServerIds();

        boolean result = subordinateIds.stream().anyMatch(subordinateId -> serverIds.stream().anyMatch(serverId -> {
            HokageSubordinateServerDO subordinateServerDO = subordinateServerDao.queryBySubordinateIdAndServerId(subordinateId, serverId);
            if (Objects.nonNull(subordinateServerDO)) {
                return true;
            }

            ServiceResponse<Long> primaryKeyRes = sequenceService.nextValue(SequenceNameEnum.hokage_subordinate_server.name());
            subordinateServerDO = new HokageSubordinateServerDO();
            subordinateServerDO.setId(primaryKeyRes.getData());
            subordinateServerDO.setSubordinateId(subordinateId);
            subordinateServerDO.setServerId(serverId);

            return subordinateServerDao.insert(subordinateServerDO) > 0;
        }));

        if (result) {
            return response.success(true);
        }

        return response.fail(ResultCodeEnum.SERVER_SYSTEM_ERROR.getCode(), ResultCodeEnum.SERVER_SYSTEM_ERROR.getMsg());
    }

    @Override
    public ServiceResponse<Boolean> revokeSubordinate(ServerOperateForm form) {
        Long operatorId = checkNotNull(form.getId(), "operator id can't null");
        checkState(!CollectionUtils.isEmpty(form.getUserIds()));
        checkState(!CollectionUtils.isEmpty(form.getServerIds()));

        ServiceResponse<Boolean> response = new ServiceResponse<>();

        ServiceResponse<Boolean> isSupervisorResponse = userService.isSupervisor(operatorId);

        if (!isSupervisorResponse.getSucceeded()) {
            return response.fail(isSupervisorResponse.getCode(), isSupervisorResponse.getMsg());
        }

        if (!isSupervisorResponse.getData()) {
            return response.fail(ResultCodeEnum.USER_NO_PERMISSION.getCode(), ResultCodeEnum.USER_NO_PERMISSION.getMsg());
        }

        List<Long> userIds = form.getUserIds();
        List<Long> serverIds = form.getServerIds();

        boolean result = userIds.stream().anyMatch(userId -> subordinateServerDao.removeBySubordinateId(userId, serverIds) > 0);

        return response.success(result);
    }

    @Override
    public ServiceResponse<Boolean> applyServer(ServerOperateForm form) {
        Long operatorId = checkNotNull(form.getId(), "operator id can't null");
        checkState(!CollectionUtils.isEmpty(form.getServerIds()));

        ServiceResponse<Boolean> response = new ServiceResponse<>();

        ServiceResponse<Integer> roleResponse = userService.getRoleByUserId(operatorId);

        if (!roleResponse.getSucceeded() || Objects.isNull(roleResponse.getData())) {
            return response.fail(roleResponse.getCode(), roleResponse.getMsg());
        }

        Function<ServerOperateForm, Boolean> applyFunction = SERVER_APPLY_MAP.get(roleResponse.getData());

        if (Objects.isNull(applyFunction)) {
            throw new RuntimeException("Unsupported server apply: " + JSON.toJSONString(form));
        }

        Boolean result = applyFunction.apply(form);

        return response.success(result);
    }

    @Override
    public ServiceResponse<List<HokageServerVO>> listSupervisorGrantServer(Long supervisorId) {
        ServiceResponse<List<HokageServerVO>> response = new ServiceResponse<>();
        List<HokageServerDO> serverDOList = hokageServerDao.selectBySupervisorId(supervisorId);
        List<HokageServerVO> serverVOList = serverDOList.stream()
                .map(serverDO -> ServerDOConverter.converterDO2VO(serverDO, ConverterTypeEnum.all))
                .collect(Collectors.toList());

        return response.success(serverVOList);
    }

    @Override
    public ServiceResponse<List<HokageServerVO>> listNotGrantServer(Long supervisorId) {
        ServiceResponse<List<HokageServerVO>> response = new ServiceResponse<>();
        List<HokageServerDO> allServer = hokageServerDao.selectAll();
        List<Long> grantServerIdList = hokageServerDao.selectBySupervisorId(supervisorId).stream().map(HokageServerDO::getId).collect(Collectors.toList());

        List<HokageServerDO> notGrantServerList = allServer.stream().filter(serverDO -> !grantServerIdList.contains(serverDO.getId())).collect(Collectors.toList());
        List<HokageServerVO> serverVOList = notGrantServerList.stream()
                .map(serverDO -> ServerDOConverter.converterDO2VO(serverDO, ConverterTypeEnum.all))
                .collect(Collectors.toList());

        return response.success(serverVOList);
    }
}
