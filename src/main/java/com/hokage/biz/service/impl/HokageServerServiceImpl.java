package com.hokage.biz.service.impl;

import com.hokage.biz.converter.server.ConverterTypeEnum;
import com.hokage.biz.converter.server.ServerDOConverter;
import com.hokage.biz.enums.LoginTypeEnum;
import com.hokage.biz.enums.SequenceNameEnum;
import com.hokage.biz.enums.ResultCodeEnum;
import com.hokage.biz.enums.UserRoleEnum;
import com.hokage.biz.form.server.HokageServerForm;
import com.hokage.biz.form.server.ServerOperateForm;
import com.hokage.biz.request.command.MonitorParam;
import com.hokage.biz.request.server.AllServerQuery;
import com.hokage.biz.request.server.ServerQuery;
import com.hokage.biz.request.server.SubordinateServerQuery;
import com.hokage.biz.request.server.SupervisorServerQuery;
import com.hokage.biz.response.resource.general.LastLogInfoVO;
import com.hokage.biz.response.server.HokageServerVO;
import com.hokage.biz.response.server.ServerAccountVO;
import com.hokage.biz.service.HokageSequenceService;
import com.hokage.biz.service.HokageServerService;
import com.hokage.biz.service.HokageUserService;
import com.hokage.cache.HokageServerCacheDao;
import com.hokage.common.ServiceResponse;
import com.hokage.persistence.dao.*;
import com.hokage.persistence.dataobject.*;
import com.google.common.collect.ImmutableMap;
import com.hokage.ssh.SshClient;
import com.hokage.ssh.command.handler.MonitorCommandHandler;
import com.hokage.util.TimeUtil;
import org.apache.commons.lang3.StringUtils;
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
    private HokageSequenceService sequenceService;
    private HokageSupervisorServerDao supervisorServerDao;
    private HokageSubordinateServerDao subordinateServerDao;
    private HokageUserDao userDao;
    private HokageServerApplicationDao applicationDao;
    private HokageServerSshKeyContentDao contentDao;
    private HokageSupervisorSubordinateDao supSubDao;

    private MonitorCommandHandler<MonitorParam> monitorCommandHandler;
    private HokageServerCacheDao serverCacheDao;

    @Autowired
    public void setHokageServerDao(HokageServerDao hokageServerDao) {
        this.hokageServerDao = hokageServerDao;
    }

    @Autowired
    public void setUserService(HokageUserService userService) {
        this.userService = userService;
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
    public void setContentDao(HokageServerSshKeyContentDao contentDao) {
        this.contentDao = contentDao;
    }

    @Autowired
    public void setSupSubDao(HokageSupervisorSubordinateDao supSubDao) {
        this.supSubDao = supSubDao;
    }

    @Autowired
    public void setServerCacheDao(HokageServerCacheDao serverCacheDao) {
        this.serverCacheDao = serverCacheDao;
    }

    @Autowired
    public void setMonitorCommandHandler(MonitorCommandHandler<MonitorParam> monitorCommandHandler) {
        this.monitorCommandHandler = monitorCommandHandler;
    }

    private final ImmutableMap<Integer, Function<ServerOperateForm, Boolean>> SERVER_APPLY_MAP =
            ImmutableMap.<Integer, Function<ServerOperateForm, Boolean>>builder()
                    .put(UserRoleEnum.supervisor.getValue(), (form -> {
                        List<Long> serverIds = form.getServerIds();
                        return serverIds.stream().anyMatch(serverId -> {
                            HokageServerApplicationDO applicationDO = new HokageServerApplicationDO();

                            applicationDO.setApplyId(form.getOperatorId())
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

                                applicationDO.setApplyId(form.getOperatorId())
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
                .map(serverDO -> ServerDOConverter.converter2VO(serverDO, ConverterTypeEnum.all))
                .collect(Collectors.toList());
        response.success(serverVOList);
        return response;
    }

    @Override
    public ServiceResponse<List<HokageServerVO>> searchAllServer(AllServerQuery allServerQuery) {
        ServiceResponse<List<HokageServerVO>> response = new ServiceResponse<>();

        List<HokageServerDO> serverDOList = hokageServerDao.selectByAllQuery(allServerQuery);
        List<HokageServerVO> serverVOList = serverDOList.stream()
                .map(serverDO -> ServerDOConverter.converter2VO(serverDO, ConverterTypeEnum.all))
                .collect(Collectors.toList());

        return response.success(serverVOList);
    }

    @Override
    public ServiceResponse<List<HokageServerVO>> searchSupervisorServer(SupervisorServerQuery query) {
        ServiceResponse<List<HokageServerVO>> response = new ServiceResponse<>();
        List<HokageServerDO> serverDOList = hokageServerDao.selectBySupervisorQuery(query);
        List<HokageServerVO> serverVOList = serverDOList.stream()
                .map(serverDO -> ServerDOConverter.converter2VO(serverDO, ConverterTypeEnum.supervisor))
                .collect(Collectors.toList());

        return response.success(serverVOList);
    }

    @Override
    public ServiceResponse<List<HokageServerVO>> searchSubordinateServer(SubordinateServerQuery query) {
        ServiceResponse<List<HokageServerVO>> response = new ServiceResponse<>();
        List<HokageServerVO> serverVOList;
        // 超级管理员返回全部使用账号
        if (UserRoleEnum.super_operator.getValue().equals(query.getRole())) {
            query.setOperatorId(0L);
            serverVOList = hokageServerDao.selectSubordinateServer(query)
                    .stream()
                    .map(serverDO -> {
                        HokageServerVO serverVO = ServerDOConverter.converter2VO(serverDO, ConverterTypeEnum.subordinate);
                        return subordinateServerDao.listByServerIds(Collections.singletonList(serverDO.getId()))
                                .stream()
                                .map(subServerDO -> {
                                    serverVO.setAccount(subServerDO.getAccount());
                                    return serverVO;
                                }).collect(Collectors.toList());
                    })
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
        } else {
            List<HokageServerDO> serverDOList = hokageServerDao.selectSubordinateServer(query);
            serverVOList = serverDOList.stream()
                    .map(serverDO -> {
                        HokageServerVO serverVO = ServerDOConverter.converter2VO(serverDO, ConverterTypeEnum.subordinate);
                        HokageSubordinateServerDO subordinateServerDO = subordinateServerDao.queryBySubordinateIdAndServerId(query.getOperatorId(), serverDO.getId());
                        serverVO.setAccount(subordinateServerDO.getAccount());
                        return serverVO;
                    })
                    .collect(Collectors.toList());
        }


        return response.success(serverVOList);
    }

    @Override
    public ServiceResponse<List<HokageServerVO>> searchServer(ServerQuery query) {
        ServiceResponse<List<HokageServerVO>> response = new ServiceResponse<>();
        List<HokageServerDO> serverDOList = new ArrayList<>();
        if (UserRoleEnum.super_operator.getValue().equals(query.getRole())) {
            serverDOList = hokageServerDao.selectAll();
        } else if (UserRoleEnum.supervisor.getValue().equals(query.getRole())) {
            serverDOList = hokageServerDao.selectBySupervisorId(query.getOperatorId());
        } else if (UserRoleEnum.subordinate.getValue().equals(query.getRole())) {
            List<HokageSubordinateServerDO> subServerDOList = subordinateServerDao.listByOrdinateIds(Collections.singletonList(query.getOperatorId()));
            serverDOList = subServerDOList.stream().map(subServerDO -> {
                HokageServerDO serverDO = hokageServerDao.selectById(subServerDO.getServerId());
                serverDO.setAccount(subServerDO.getAccount());
                return serverDO;
            }).collect(Collectors.toList());
        }
        List<HokageServerVO> serverVOList = serverDOList
                .stream()
                .map(serverDO -> ServerDOConverter.converter2VO(serverDO, ConverterTypeEnum.all))
                .collect(Collectors.toList());
        return response.success(serverVOList);
    }

    @Override
    public ServiceResponse<List<ServerAccountVO>> listServerAccount(Long id) {
        ServiceResponse<List<ServerAccountVO>> response = new ServiceResponse<>();
        List<HokageSubordinateServerDO> subServerDOList = subordinateServerDao.listByServerIds(Collections.singletonList(id));
        List<ServerAccountVO> serverAccountVOList = subServerDOList.stream()
                .map(subServerDO -> {
                    HokageUserDO userDO = userDao.getUserById(subServerDO.getSubordinateId());

                    ServerAccountVO serverAccountVO = new ServerAccountVO();
                    serverAccountVO.setId(userDO.getId());
                    serverAccountVO.setAccount(subServerDO.getAccount());
                    serverAccountVO.setUsername(userDO.getUsername());
                    String createdTime = TimeUtil.format(subServerDO.getGmtCreate().getTime(), TimeUtil.DISPLAY_FORMAT);
                    serverAccountVO.setCreatedTime(createdTime);

                    try {
                        Optional<SshClient> optional = serverCacheDao.getExecClient(subServerDO.buildKey());
                        if (optional.isPresent()) {
                            MonitorParam param = new MonitorParam();
                            param.setAccount(subServerDO.getAccount());
                            ServiceResponse<List<LastLogInfoVO>> result = monitorCommandHandler.lastLogInfoHandler.apply(optional.get(), param);
                            if (result.getSucceeded() && !CollectionUtils.isEmpty(result.getData())) {
                                LastLogInfoVO lastLogInfoVO = result.getData().get(0);
                                serverAccountVO.setLatestLoginTime(lastLogInfoVO.getLatest());
                            }
                        }
                    } catch (Exception ignored) {

                    }

                    return serverAccountVO;
                }).collect(Collectors.toList());

        return response.success(serverAccountVOList);
    }

    @Override
    public ServiceResponse<HokageServerVO> viewServer(Long id) {
        ServiceResponse<HokageServerVO> response = new ServiceResponse<>();
        HokageServerDO serverDO = hokageServerDao.selectById(id);
        if (Objects.isNull(serverDO)) {
            return response.success(null);
        }
        return response.success(ServerDOConverter.converter2VO(serverDO, ConverterTypeEnum.all));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServiceResponse<Boolean> revokeSubordinate(ServerOperateForm form) {
        checkState(!CollectionUtils.isEmpty(form.getUserIds()));
        checkState(!CollectionUtils.isEmpty(form.getServerIds()));

        ServiceResponse<Boolean> response = new ServiceResponse<>();

        List<Long> userIdList = form.getUserIds();
        List<Long> revokeServerIdList = form.getServerIds();

        for (Long userId : userIdList) {
            List<Long> subordinateIdList = supSubDao.listBySupervisorId(userId)
                    .stream()
                    .map(HokageSupervisorSubordinateDO::getSubordinateId)
                    .collect(Collectors.toList());

            List<Long> serverIdList = subordinateServerDao.listByOrdinateIds(subordinateIdList)
                    .stream()
                    .map(HokageSubordinateServerDO::getServerId)
                    .collect(Collectors.toList());


            if (CollectionUtils.containsAny(serverIdList, revokeServerIdList)) {
                throw new RuntimeException("服务器存在使用者，请先回收其账号");
            }

            subordinateServerDao.removeBySubordinateId(userId, revokeServerIdList);
        }
        return response.success(Boolean.TRUE);
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
    @Transactional(rollbackFor = Exception.class)
    public ServiceResponse<HokageServerDO> save(HokageServerDO serverDO) {
        ServiceResponse<HokageServerDO> response = new ServiceResponse<>();
        // set ssh key content as password if loginType = 1
        setPasswordContent(serverDO);
        long result = upsertServer(serverDO);
        if (result > 0) {
            return response.success(serverDO);
        }
        return response.fail(ResultCodeEnum.SERVER_SYSTEM_ERROR.getCode(), "save server error.");
    }

    /**
     * insert or update server
     * @param serverDO {@link HokageServerDO}
     */
    private Long upsertServer(HokageServerDO serverDO) {
        if (Objects.isNull(serverDO.getId()) || serverDO.getId()  == 0) {
            // insert
            ServiceResponse<Long> primaryKeyResponse = sequenceService.nextValue(SequenceNameEnum.hokage_server.name());
            if (!primaryKeyResponse.getSucceeded() || primaryKeyResponse.getData() <= 0) {
                throw new RuntimeException("sequenceService get hokage_server primary key value error.");
            }
            serverDO.setId(primaryKeyResponse.getData());

            return hokageServerDao.insert(serverDO);

        } else {
            // update
            return hokageServerDao.update(serverDO);
        }
    }

    /**
     * add supervisor when add server
     * @param serverDO {@link HokageServerDO}
     * @param form {@link HokageServerForm}
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
     * @param serverDO {@link HokageServerDO}
     */
    private void setPasswordContent(HokageServerDO serverDO) {
        if (LoginTypeEnum.key.getValue().equals(serverDO.getLoginType())) {
            HokageServerSshKeyContentDO contentDO = contentDao.listByUid(serverDO.getPasswd());
            if (Objects.isNull(contentDO)) {
                throw new RuntimeException("ssh key file content id empty. uid: " + serverDO.getPasswd());
            }
            serverDO.setPasswd(contentDO.getContent());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServiceResponse<Boolean> delete(Long serverId) {
        ServiceResponse<Boolean> response = new ServiceResponse<>();
        // 1. 服务器是否存在使用者
        List<HokageSubordinateServerDO> subServerDOList = subordinateServerDao.listByServerIds(Collections.singletonList(serverId));
        if (!CollectionUtils.isEmpty(subServerDOList)) {
            return response.fail(StringUtils.EMPTY, "服务器存在使用者, 请先回收相关账号");
        }
        // 2. 服务器是否存在管理者
        List<HokageSupervisorServerDO> supServerDOList = supervisorServerDao.listByServerIds(Collections.singletonList(serverId));
        if (!CollectionUtils.isEmpty(supServerDOList)) {
            return response.fail(StringUtils.EMPTY, "服务器存在管理者, 请先移除相关管理员");
        }

        HokageServerDO serverDO = hokageServerDao.selectById(serverId);
        if (Objects.isNull(serverDO)) {
            throw new RuntimeException("server is not exists, server id: " + serverId);
        }
        long result = hokageServerDao.deleteById(serverId);
        if (result > 0) {
            return response.success(Boolean.TRUE);
        }
        return response.fail(ResultCodeEnum.SERVER_SYSTEM_ERROR.getCode(), "更新数据库失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServiceResponse<Boolean> designateSupervisor(ServerOperateForm form) {

        Long operatorId = checkNotNull(form.getOperatorId(), "id can't be null");
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
    public ServiceResponse<Boolean> designateSubordinate(ServerOperateForm form) {
        Long operatorId = checkNotNull(form.getOperatorId(), "id can't be null");
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
    public ServiceResponse<List<HokageServerVO>> listSupervisorGrantServer(Long supervisorId) {
        ServiceResponse<List<HokageServerVO>> response = new ServiceResponse<>();
        List<HokageServerDO> serverDOList = hokageServerDao.selectBySupervisorId(supervisorId);
        List<HokageServerVO> serverVOList = serverDOList.stream()
                .map(serverDO -> ServerDOConverter.converter2VO(serverDO, ConverterTypeEnum.all))
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
                .map(serverDO -> ServerDOConverter.converter2VO(serverDO, ConverterTypeEnum.all))
                .collect(Collectors.toList());

        return response.success(serverVOList);
    }
}
