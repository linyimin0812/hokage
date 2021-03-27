package com.hokage.biz.service.impl;

import com.hokage.biz.converter.server.ConverterTypeEnum;
import com.hokage.biz.converter.user.UserConverter;
import com.hokage.biz.enums.SequenceNameEnum;
import com.hokage.biz.enums.ResultCodeEnum;
import com.hokage.biz.enums.UserRoleEnum;
import com.hokage.biz.form.user.UserServerSearchForm;
import com.hokage.biz.request.UserQuery;
import com.hokage.biz.response.user.HokageUserVO;
import com.hokage.biz.service.HokageSequenceService;
import com.hokage.biz.service.HokageUserService;
import com.hokage.common.ServiceResponse;
import com.hokage.persistence.dao.*;
import com.hokage.persistence.dataobject.*;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
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
    public ServiceResponse<List<HokageUserVO>> listSupervisors() {

        List<HokageUserDO> users = userDao.listUserByRole(UserRoleEnum.supervisor.getValue());

        List<HokageUserVO> userVOList = users.stream().map(this::supervisorUserDO2UserVO).collect(Collectors.toList());

        return success(userVOList);

    }

    @Override
    public ServiceResponse<List<HokageUserVO>> searchSupervisors(UserQuery query) {

        query.setRole(UserRoleEnum.supervisor.getValue());

        List<HokageUserDO>  supervisorList = userDao.query(query);
        if (CollectionUtils.isEmpty(supervisorList)) {
            return success(Collections.emptyList());
        }

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
    public ServiceResponse<Boolean> deleteSupervisor(List<Long> ids) {
        checkNotNull(ids, "supervisor ids can't be null");

        boolean isSucceed = ids.stream().allMatch(id -> {
            HokageUserDO userDO = new HokageUserDO();
            userDO.setId(id);
            userDO.setRole(UserRoleEnum.subordinate.getValue());

            if (userDao.update(userDO) <= 0) {
                return Boolean.FALSE;
            }

            return supervisorServerDao.removeBySupervisorId(id) > 0;
        });
        if (isSucceed) {
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
    public ServiceResponse<Boolean> recycleSupervisor(Long id, List<Long> serverIds) {
        checkNotNull(id, "supervisor id can't be null");
        checkNotNull(id, "serverIds can't be null");

        boolean isSucceed = supervisorServerDao.removeBySupervisorId(id, serverIds) > 0;
        if (isSucceed) {
            return success(Boolean.TRUE);
        }
        return fail("A-XXX", "HokageUserDO recycleSupervisor error");
    }

    @Override
    public ServiceResponse<Boolean> grantSupervisor(Long id, List<Long> serverIds) {
        checkNotNull(id, "supervisor id can't be null");
        checkNotNull(id, "serverIds can't be null");

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

    /**
     * search subordinate
     * @param form
     * @return
     */
    @Override
    public ServiceResponse<List<HokageUserVO>> searchSubordinates(UserServerSearchForm form) {

        List<HokageUserDO>  supervisorList;
        if (Objects.nonNull(form.getId()) && form.getId() > 0) {
            HokageUserDO userDO = userDao.getUserById(form.getId());
            supervisorList = Collections.singletonList(userDO);
        } else if (!CollectionUtils.isEmpty(form.getServerGroup())) {
            // 1. retrieve server info by label
            List<HokageServerDO> serverDOList = serverDao.selectByGroup(String.join(",", form.getServerGroup()));
            // 2. retrieve subordinate ids by server ids
            List<Long> serverIds = serverDOList.stream().map(HokageServerDO::getId).collect(Collectors.toList());
            List<HokageSubordinateServerDO> subordinateServerDOList = subordinateServerDao.listByServerIds(serverIds);
            // 3. retrieve subordinate info based on subordinate ids
            List<Long> userIds = subordinateServerDOList.stream().map(HokageSubordinateServerDO::getId).collect(Collectors.toList());
            supervisorList = userDao.listUserByIds(userIds);
        } else {
            HokageUserDO hokageUserDO = new HokageUserDO();
            hokageUserDO.setUsername(form.getUsername());
            supervisorList = userDao.listAll(hokageUserDO);
        }

        List<HokageUserVO> userVOList = supervisorList.stream().map(this::subordinateUserDO2UserVO).collect(Collectors.toList());

        return success(userVOList);

    }

    @Override
    public ServiceResponse<Boolean> addSubordinate(Long supervisorId, List<Long> ids) {

        List<HokageSupervisorSubordinateDO> subordinateDOList = supervisorSubordinateDao.listSubordinate(supervisorId, ids);

        List<HokageSupervisorSubordinateDO> supervisorSubordinateDOList = ids.stream().filter(id -> {
            // if subordinate has existed, filter
            return subordinateDOList.stream().noneMatch(DO -> DO.getSubordinateId().equals(id));
        }).map(id -> {
            HokageSupervisorSubordinateDO supervisorSubordinateDO  = new HokageSupervisorSubordinateDO();

            // retrieve id
            ServiceResponse<Long> response = sequenceService.nextValue(SequenceNameEnum.hokage_supervisor_subordinate.name());
            if (!response.getSucceeded() || ObjectUtils.defaultIfNull(response.getData(), 0L) == 0) {
                throw new RuntimeException("addSubordinate: can't retrieve primary key id, reason: " + response.getMsg());
            }

            supervisorSubordinateDO.setId(response.getData());
            supervisorSubordinateDO.setSupervisorId(supervisorId);
            supervisorSubordinateDO.setSubordinateId(id);

            return supervisorSubordinateDO;
        }).collect(Collectors.toList());

        Long result = supervisorSubordinateDao.insertBatch(supervisorSubordinateDOList);
        if (result > 0) {
            return success(Boolean.TRUE);
        }
        return fail("A-XXX", "HokageUserDO addSupervisor error");
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
    public ServiceResponse<Boolean> grantSubordinate(Long id, List<Long> serverIds) {
        boolean isSucceed = subordinateServerDao.addBySubordinateId(id, serverIds) > 0;
        if (isSucceed) {
            return success(Boolean.TRUE);
        }
        return fail("A-XXX", "HokageUserDO grantSupervisor error");
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

    private HokageUserVO supervisorUserDO2UserVO(HokageUserDO userDO) {
        checkNotNull(userDO, "userDO can't be null");
        return UserConverter.converter(userDO, ConverterTypeEnum.supervisor);
    }


    private HokageUserVO subordinateUserDO2UserVO(HokageUserDO userDO) {
        checkNotNull(userDO, "userDO can't be null");
        return UserConverter.converter(userDO, ConverterTypeEnum.subordinate);
    }

}
