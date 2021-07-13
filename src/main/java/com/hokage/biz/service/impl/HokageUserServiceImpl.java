package com.hokage.biz.service.impl;

import com.hokage.biz.converter.server.ConverterTypeEnum;
import com.hokage.biz.converter.server.ServerDOConverter;
import com.hokage.biz.converter.user.UserConverter;
import com.hokage.biz.enums.RecordStatusEnum;
import com.hokage.biz.enums.SequenceNameEnum;
import com.hokage.biz.enums.ResultCodeEnum;
import com.hokage.biz.enums.UserRoleEnum;
import com.hokage.biz.request.command.AccountParam;
import com.hokage.biz.request.server.SubordinateServerQuery;
import com.hokage.biz.request.user.SubordinateQuery;
import com.hokage.biz.request.user.SupervisorQuery;
import com.hokage.biz.response.server.HokageServerVO;
import com.hokage.biz.response.user.HokageUserVO;
import com.hokage.biz.service.HokageSequenceService;
import com.hokage.biz.service.HokageUserService;
import com.hokage.common.ServiceResponse;
import com.hokage.persistence.dao.*;
import com.hokage.persistence.dataobject.*;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author linyimin
 * @date: 2020/8/30 2:18pm
 * @email linyimin520812@gmail.com
 * @description
 */
@Service
public class HokageUserServiceImpl extends HokageServiceResponse implements HokageUserService {

    private HokageUserDao userDao;
    private BCryptPasswordEncoder passwordEncoder;
    private HokageSequenceService sequenceService;
    private HokageSupervisorServerDao supervisorServerDao;
    private HokageServerDao serverDao;
    private HokageSubordinateServerDao subordinateServerDao;
    private HokageSupervisorSubordinateDao supervisorSubordinateDao;
    private HokageAccountService accountService;

    @Autowired
    public void setUserDao(HokageUserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setSequenceService(HokageSequenceService sequenceService) {
        this.sequenceService = sequenceService;
    }

    @Autowired
    public void setSupervisorServerDao(HokageSupervisorServerDao supervisorServerDao) {
        this.supervisorServerDao = supervisorServerDao;
    }

    @Autowired
    public void setSubordinateServerDao(HokageSubordinateServerDao subordinateServerDao) {
        this.subordinateServerDao = subordinateServerDao;
    }

    @Autowired
    public void setServerDao(HokageServerDao serverDao) {
        this.serverDao = serverDao;
    }

    @Autowired
    public void setSupervisorSubordinateDao(HokageSupervisorSubordinateDao hokageSupervisorSubordinateDao) {
        this.supervisorSubordinateDao = hokageSupervisorSubordinateDao;
    }

    @Autowired
    public void setAccountService(HokageAccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServiceResponse<HokageUserDO> register(HokageUserDO hokageUserDO) {

        // 1. determine whether the mail already exists
        HokageUserDO userDO = userDao.getUserByEmail(hokageUserDO.getEmail());
        if (Objects.nonNull(userDO)) {
            return fail(ResultCodeEnum.USERNAME_DUPLICATE_ERROR.getCode(), ResultCodeEnum.USERNAME_DUPLICATE_ERROR.getMsg());
        }
        // 2. encrypt the password
        hokageUserDO.setPasswd(passwordEncoder.encode(hokageUserDO.getPasswd()));
        // 3. the registering user is ordinary as default
        hokageUserDO.setRole(UserRoleEnum.subordinate.getValue());

        // retrieve user id
        ServiceResponse<Long> sequence = sequenceService.nextValue(SequenceNameEnum.hokage_user.name());

        if (sequence.getSucceeded()) {
            hokageUserDO.setId(sequence.getData());
        } else {
            return fail(sequence.getCode(), sequence.getMsg());
        }

        hokageUserDO.setStatus(RecordStatusEnum.inuse.getStatus());

        Long result = userDao.insert(hokageUserDO);
        if (result > 0) {
            return success(hokageUserDO);
        }
        return fail(ResultCodeEnum.USER_REGISTER_FAIL.getCode(), ResultCodeEnum.USER_REGISTER_FAIL.getMsg());
    }

    @Override
    public ServiceResponse<HokageUserDO> login(HokageUserDO hokageUserDO) {

        // 1. determine whether the mail already exists
        HokageUserDO userDO = userDao.getUserByEmail(hokageUserDO.getEmail());
        if (Objects.isNull(userDO)) {
            return fail(ResultCodeEnum.USER_PASSWD_ERROR.getCode(), ResultCodeEnum.USER_PASSWD_ERROR.getMsg());
        }

        // 2. verify password
        boolean isMatch = passwordEncoder.matches(hokageUserDO.getPasswd(), userDO.getPasswd());

        return isMatch ? success(userDO) : fail(ResultCodeEnum.USER_PASSWD_ERROR.getCode(), ResultCodeEnum.USER_PASSWD_ERROR.getMsg());
    }

    @Override
    public ServiceResponse<List<HokageUserVO>> searchSubordinate(SubordinateQuery query) {
        HokageUserDO userDO = userDao.getUserById(query.getOperatorId());
        if (Objects.isNull(userDO)) {
            return fail(ResultCodeEnum.SERVER_SYSTEM_ERROR.getCode(), "user is not exists. user id: " + query.getOperatorId());
        }
        // 超级用户默认查询所有
        if (UserRoleEnum.super_operator.getValue().equals(userDO.getRole())) {
            query.setOperatorId(0L);
        }
        List<HokageUserDO> subordinateList = userDao.querySubordinate(query);
        List<HokageUserVO> userVOList = subordinateList.stream().map(this::subordinateUserDO2UserVO).collect(Collectors.toList());
        return success(userVOList);
    }

    @Override
    public ServiceResponse<List<HokageUserVO>> searchSupervisor(SupervisorQuery query) {
        List<HokageUserDO> supervisorList = userDao.querySupervisor(query);
        List<HokageUserVO> userVOList = supervisorList.stream().map(this::supervisorUserDO2UserVO).collect(Collectors.toList());
        return success(userVOList);
    }

    @Override
    public ServiceResponse<Boolean> addSupervisor(List<Long> ids) {
        checkNotNull(ids, "supervisor ids can't be null");

        boolean isSucceed = ids.stream().allMatch(id -> {
            HokageUserDO userDO = new HokageUserDO();
            userDO.setId(id);
            userDO.setRole(UserRoleEnum.supervisor.getValue());
            return userDao.update(userDO) > 0;
        });
        if (isSucceed) {
            return success(Boolean.TRUE);
        }
        return fail("A-XXX", "HokageUserDO addSupervisor error");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServiceResponse<Boolean> deleteSupervisor(Long id) {
        checkNotNull(id, "supervisor id can't be null");
        // 1. 删除其管理的用户关联的服务器
        List<Long> subIdList = supervisorSubordinateDao.listBySupervisorId(id)
                .stream()
                .map(HokageSupervisorSubordinateDO::getSubordinateId)
                .collect(Collectors.toList());

        for (Long subId : subIdList) {
            long result = subordinateServerDao.removeBySubordinateId(subId);
            if (result < 0 ) {
                throw new RuntimeException("delete subordinate server error.");
            }
        }

        // 2. 删除管理的用户
        long result = supervisorSubordinateDao.deleteSupervisor(id);
        if (result < 0) {
            throw new RuntimeException("delete supervisor subordinate error.");
        }

        // 3. 删除管理的服务器
        result = supervisorServerDao.removeBySupervisorId(id);
        if (result < 0) {
            throw new RuntimeException("delete supervisor server error.");
        }

        // 4. 将管理员角色设置为普通角色
        HokageUserDO userDO = new HokageUserDO();
        userDO.setId(id);
        userDO.setRole(UserRoleEnum.subordinate.getValue());

        if (userDao.update(userDO) > 0) {
            return success(Boolean.TRUE);
        }
        return fail("A-XXX", "HokageUserDO deleteSupervisor error");
    }

    @Override
    public ServiceResponse<Boolean> recycleSupervisor(Long id) {
        checkNotNull(id, "supervisor ids can't be null");

        boolean isSucceed = supervisorServerDao.removeBySupervisorId(id) > 0;
        if (isSucceed) {
            return success(Boolean.TRUE);
        }
        return fail("A-XXX", "HokageUserDO recycleSupervisor error");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServiceResponse<Boolean> recycleSupervisor(Long id, List<Long> serverIds) {
        checkNotNull(id, "supervisor id can't be null");
        checkNotNull(id, "serverIds can't be null");

        boolean isSucceed = serverIds.size() == supervisorServerDao.removeBySupervisorId(id, serverIds);
        if (!isSucceed) {
            throw new RuntimeException("recycle supervisor server error.");
        }
        List<HokageSupervisorSubordinateDO> supSubDOList = supervisorSubordinateDao.listBySupervisorId(id);
        if (CollectionUtils.isEmpty(supSubDOList)) {
            return success(Boolean.TRUE);
        }
        isSucceed = supSubDOList.stream().allMatch(supSubDO -> subordinateServerDao.removeBySubordinateId(supSubDO.getSubordinateId(), serverIds) > 0);
        if (!isSucceed) {
            throw new RuntimeException("remove subordinate server error.");
        }
        return success(Boolean.TRUE);
    }

    @Override
    public ServiceResponse<Boolean> grantSupervisor(Long id, List<Long> serverIds) {
        checkNotNull(id, "supervisor id can't be null");
        checkNotNull(id, "serverIds can't be null");

        List<Long> exitedServerList = supervisorServerDao.listBySupervisorIds(Collections.singletonList(id)).stream()
                .map(HokageSupervisorServerDO::getServerId)
                .collect(Collectors.toList());

        serverIds = serverIds.stream().filter(serverId -> !exitedServerList.contains(serverId)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(serverIds)) {
            return success(Boolean.TRUE);
        }
        boolean isSucceed = supervisorServerDao.addBySupervisorId(id, serverIds) > 0;
        if (isSucceed) {
            return success(Boolean.TRUE);
        }
        return fail("A-XXX", "HokageUserDO grantSupervisor error");
    }

    @Override
    public ServiceResponse<List<HokageUserVO>> listOrdinaryUsers(Long supervisorId) {
        HokageUserDO userDO = userDao.getUserById(supervisorId);

        if (Objects.isNull(userDO)) {
            return success(Collections.emptyList());
        }

        List<HokageUserDO> userDOList;
        // if supervisorId is a super, list all ordinary user
        if (UserRoleEnum.super_operator.getValue().equals(userDO.getRole())) {
            userDOList = userDao.listUserByRole(UserRoleEnum.subordinate.getValue());
        } else {
            List<Long> userIdList = supervisorSubordinateDao.listBySupervisorId(supervisorId).stream()
                    .map(HokageSupervisorSubordinateDO::getSubordinateId)
                    .collect(Collectors.toList());
            userDOList = userDao.listUserByIds(userIdList);
        }
        List<HokageUserVO> userVOList = userDOList.stream().map(this::subordinateUserDO2UserVO).collect(Collectors.toList());

        return success(userVOList);

    }

    @Override
    public ServiceResponse<List<HokageUserVO>> listAllOrdinateUsers() {

        List<HokageUserDO> userDOList = userDao.listUserByRole(UserRoleEnum.subordinate.getValue());
        if (CollectionUtils.isEmpty(userDOList)) {

            return success(Collections.emptyList());

        }

        List<HokageUserVO> userVOList = userDOList.stream().map(this::subordinateUserDO2UserVO).collect(Collectors.toList());

        return success(userVOList);
    }

    @Override
    public ServiceResponse<Boolean> addSubordinates2Supervisor(Long supervisorId, List<Long> ids) {

        List<HokageSupervisorSubordinateDO> supervisorSubordinateDOList = ids.stream().map(id -> assembleSupSubDO(supervisorId, id)).collect(Collectors.toList());
        Long result = supervisorSubordinateDao.insertBatch(supervisorSubordinateDOList);
        if (result > 0) {
            return success(Boolean.TRUE);
        }
        return fail(ResultCodeEnum.SERVER_SYSTEM_ERROR.getCode(), "HokageUserService addSubordinates2Supervisor error");
    }

    private HokageSupervisorSubordinateDO assembleSupSubDO(Long supervisorId, Long id) {
        HokageSupervisorSubordinateDO supervisorSubordinateDO  = new HokageSupervisorSubordinateDO();

        // retrieve id
        ServiceResponse<Long> response = sequenceService.nextValue(SequenceNameEnum.hokage_supervisor_subordinate.name());
        if (!response.getSucceeded() || ObjectUtils.defaultIfNull(response.getData(), 0L) == 0) {
            throw new RuntimeException("addSubordinate: can't retrieve primary key id, reason: " + response.getMsg());
        }

        supervisorSubordinateDO.setId(response.getData());
        supervisorSubordinateDO.setSupervisorId(supervisorId);
        supervisorSubordinateDO.setSubordinateId(id);
        supervisorSubordinateDO.setStatus(RecordStatusEnum.inuse.getStatus());
        return supervisorSubordinateDO;
    }

    @Override
    public ServiceResponse<Boolean> deleteSubordinate(Long supervisorId, List<Long> ids) {

        Long result = supervisorSubordinateDao.deleteSubordinate(supervisorId, ids);
        if (result > 0) {
            return success(Boolean.TRUE);
        }
        return fail("A-XXX", "HokageUserDO addSupervisor error");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServiceResponse<Boolean> grantServer2Subordinate(Long id, List<Long> serverIds) {
        HokageUserDO userDO = userDao.getUserById(id);
        if (Objects.isNull(userDO)) {
            throw new RuntimeException("user is not exist. user id: " + id);
        }
        for (long serverId : serverIds) {
            HokageServerDO serverDO = serverDao.selectById(serverId);
            if (Objects.isNull(serverDO)) {
                throw new RuntimeException("server is not exist. server id: " + serverId);
            }
            AccountParam param = new AccountParam();
            String account = userDO.getUsername() + "_" + serverId;
            String passwd = UUID.randomUUID().toString();
            param.setAccount(account).setPasswd(passwd);
            ServiceResponse<HokageSubordinateServerDO> response = accountService.addAccount(serverDO.buildKey(), param);
            if (!response.getSucceeded()) {
                throw new RuntimeException("add account error. exitCode: " + response.getCode() + ", errMsg: " + response.getMsg());
            }
            HokageSubordinateServerDO subServerDO = response.getData();
            subServerDO.setSubordinateId(id);
            long result = subordinateServerDao.insert(subServerDO);
            if (result <= 0) {
                return fail("A-XXX", "HokageUserDO grantSupervisor error");
            }
        }
        return success(Boolean.TRUE);
    }

    @Override
    public ServiceResponse<Boolean> recycleSubordinate(Long id) {

        boolean isSucceed = subordinateServerDao.removeBySubordinateId(id) > 0;
        if (isSucceed) {
            return success(Boolean.TRUE);
        }
        return fail("A-XXX", "HokageUserDO recycleSupervisor error");
    }

    @Override
    public ServiceResponse<Boolean> recycleSubordinate(Long id, List<Long> serverIds) {

        boolean isSucceed = subordinateServerDao.removeBySubordinateId(id, serverIds) > 0;
        if (isSucceed) {
            return success(Boolean.TRUE);
        }
        return fail("A-XXX", "HokageUserDO recycleSupervisor error");
    }

    @Override
    public ServiceResponse<Integer> getRoleByUserId(Long id) {
        HokageUserDO userDO = userDao.getUserById(id);
        if (Objects.isNull(userDO)) {
            return fail(ResultCodeEnum.NO_SUCH_USER.getCode(), ResultCodeEnum.NO_SUCH_USER.getMsg());
        }
        return success(userDO.getRole());
    }

    @Override
    public ServiceResponse<Boolean> isSuperOperator(Long id) {

        ServiceResponse<Integer> roleResponse = this.getRoleByUserId(id);

        if (!roleResponse.getSucceeded() || Objects.isNull(roleResponse.getData())) {
            return fail(roleResponse.getCode(), roleResponse.getMsg());
        }

        return success(UserRoleEnum.super_operator.getValue().equals(roleResponse.getData()));
    }

    @Override
    public ServiceResponse<Boolean> isSupervisor(Long id) {

        ServiceResponse<Integer> roleResponse = this.getRoleByUserId(id);

        if (!roleResponse.getSucceeded() || Objects.isNull(roleResponse.getData())) {
            return fail(roleResponse.getCode(), roleResponse.getMsg());
        }

        return success(UserRoleEnum.supervisor.getValue().equals(roleResponse.getData()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServiceResponse<Boolean> recycleSubordinates2Supervisor(Long supervisorId, List<Long> userIds) {
        boolean result = userIds.stream().allMatch(subordinateId -> {
            HokageSupervisorSubordinateDO supSubDO = new HokageSupervisorSubordinateDO();
            supSubDO.setSupervisorId(supervisorId);
            supSubDO.setSubordinateId(subordinateId);
            supSubDO.setStatus(RecordStatusEnum.deleted.getStatus());
            return supervisorSubordinateDao.update(supSubDO) > 0;
        });
        if (result) {
            return success(Boolean.TRUE);
        }
        throw new RuntimeException("recycleSubordinates2Supervisor update error.");
    }

    @Override
    public ServiceResponse<List<HokageServerVO>> searchSubordinateServer(SubordinateServerQuery query) {
        ServiceResponse<List<HokageServerVO>> response = new ServiceResponse<>();
        List<HokageSubordinateServerDO> subServerDOList = subordinateServerDao.listByOrdinateIds(Collections.singletonList(query.getSubordinateId()));

        List<Long> serverIdList = subServerDOList.stream().map(HokageSubordinateServerDO::getServerId).collect(Collectors.toList());
        Map<Long, HokageServerDO> serverDOMap = serverDao.selectByIds(serverIdList)
                .stream()
                .collect(Collectors.toMap(HokageServerDO::getId, Function.identity(), (o1, o2) -> o1));

        List<HokageServerVO> serverVOList = subServerDOList.stream()
                .map(subServerDO -> {
                    HokageServerDO serverDO = serverDOMap.get(subServerDO.getServerId());

                    HokageServerVO serverVO = new HokageServerVO();
                    serverVO.setId(serverDO.getId());
                    List<String> serverGroupList = new ArrayList<>();
                    if (StringUtils.isNotEmpty(serverDO.getServerGroup())) {
                        serverGroupList = Arrays.asList(StringUtils.split(serverDO.getServerGroup(), ","));
                    }
                    serverVO.setServerGroupList(serverGroupList);
                    serverVO.setDomain(serverDO.getDomain());
                    serverVO.setHostname(serverDO.getHostname());
                    serverVO.setDescription(serverDO.getDescription());

                    serverVO.setIp(subServerDO.getIp());
                    serverVO.setSshPort(subServerDO.getSshPort());
                    serverVO.setAccount(subServerDO.getAccount());
                    serverVO.setLoginType(subServerDO.getLoginType());

                    return serverVO;
                })
                .collect(Collectors.toList());

        return response.success(serverVOList);
    }

    private HokageUserVO supervisorUserDO2UserVO(HokageUserDO userDO) {
        checkNotNull(userDO, "userDO can't be null");
        return UserConverter.converter(userDO, ConverterTypeEnum.supervisor);
    }


    private HokageUserVO subordinateUserDO2UserVO(HokageUserDO userDO) {
        checkNotNull(userDO, "userDO can't be null");
        return UserConverter.converter(userDO, ConverterTypeEnum.subordinate);
    }

}
