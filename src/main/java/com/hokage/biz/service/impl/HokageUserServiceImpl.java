package com.hokage.biz.service.impl;

import com.hokage.biz.converter.server.ConverterTypeEnum;
import com.hokage.biz.converter.user.UserConverter;
import com.hokage.biz.enums.SequenceNameEnum;
import com.hokage.biz.enums.ErrorCodeEnum;
import com.hokage.biz.enums.UserRoleEnum;
import com.hokage.biz.form.user.UserServerSearchForm;
import com.hokage.biz.response.user.HokageUserVO;
import com.hokage.biz.service.HokageSequenceService;
import com.hokage.biz.service.HokageUserService;
import com.hokage.common.ServiceResponse;
import com.hokage.persistence.dao.*;
import com.hokage.persistence.dataobject.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class HokageUserServiceImpl implements HokageUserService {

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

        ServiceResponse<HokageUserDO> res = new ServiceResponse<>();

        // 1. determine whether the mail already exists
        HokageUserDO userDO = userDao.getUserByEmail(hokageUserDO.getEmail());
        if (Objects.nonNull(userDO)) {
            return res.fail(ErrorCodeEnum.USERNAME_DUPLICATE_ERROR.getCode(), ErrorCodeEnum.USERNAME_DUPLICATE_ERROR.getMsg());
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
            return res.fail(sequence.getCode(), sequence.getMsg());
        }

        Long result = userDao.insert(hokageUserDO);
        if (result > 0) {
            return res.success(hokageUserDO);
        }
        return res.fail(ErrorCodeEnum.USER_REGISTER_FAIL.getCode(), ErrorCodeEnum.USER_REGISTER_FAIL.getMsg());
    }

    @Override
    public ServiceResponse<HokageUserDO> login(HokageUserDO hokageUserDO) {

        ServiceResponse<HokageUserDO> res = new ServiceResponse<>();

        res.fail(ErrorCodeEnum.USER_PASSWD_ERROR.getCode(), ErrorCodeEnum.USER_PASSWD_ERROR.getMsg());

        // 1. determine whether the mail already exists
        HokageUserDO userDO = userDao.getUserByEmail(hokageUserDO.getEmail());
        if (Objects.isNull(userDO)) {
            return res;
        }

        // 2. verify password
        boolean isMatch = passwordEncoder.matches(hokageUserDO.getPasswd(), userDO.getPasswd());

        return isMatch ? res.success(userDO) : res;
    }

    @Override
    public ServiceResponse<List<HokageUserVO>> listSupervisors() {

        ServiceResponse<List<HokageUserVO>> res = new ServiceResponse<>();

        List<HokageUserDO> users = userDao.listUserByRole(UserRoleEnum.supervisor.getValue());

        List<HokageUserVO> userVOList = users.stream().map(this::supervisorUserDO2UserVO).collect(Collectors.toList());

        res.success(userVOList);

        return res;
    }

    @Override
    public ServiceResponse<List<HokageUserVO>> searchSupervisors(UserServerSearchForm form) {

        ServiceResponse<List<HokageUserVO>> res = new ServiceResponse<>();

        List<HokageUserDO>  supervisorList;
        if (Objects.nonNull(form.getId()) && form.getId() > 0) {
            HokageUserDO userDO = userDao.getUserById(form.getId());
            supervisorList = Collections.singletonList(userDO);
        } else if (StringUtils.isNoneBlank(form.getLabel())) {
            // 1. retrieve server info by label
            List<HokageServerDO> serverDOList = serverDao.listByType(form.getLabel());
            // 2. retrieve supervisor ids by server ids
            List<Long> serverIds = serverDOList.stream().map(HokageServerDO::getId).collect(Collectors.toList());
            List<HokageSupervisorServerDO> supervisorServerDOList = supervisorServerDao.listByServerIds(serverIds);
            // 3. retrieve supervisor info by supervisor ids
            List<Long> userIds = supervisorServerDOList.stream().map(HokageSupervisorServerDO::getId).collect(Collectors.toList());
            supervisorList = userDao.listUserByIds(userIds);
        } else {
            HokageUserDO hokageUserDO = new HokageUserDO();
            hokageUserDO.setUsername(form.getUsername());
            supervisorList = userDao.listAll(hokageUserDO);
        }

        List<HokageUserVO> userVOList = supervisorList.stream().map(this::supervisorUserDO2UserVO).collect(Collectors.toList());
        res.success(userVOList);

        return res;
    }

    @Override
    public ServiceResponse<Boolean> addSupervisor(List<Long> ids) {
        checkNotNull(ids, "supervisor ids can't be null");

        ServiceResponse<Boolean> res = new ServiceResponse<>();
        boolean isSucceed = ids.stream().allMatch(id -> {
            HokageUserDO userDO = new HokageUserDO();
            userDO.setId(id);
            userDO.setRole(UserRoleEnum.supervisor.getValue());
            return userDao.update(userDO) > 0;
        });
        if (isSucceed) {
            return res.success(Boolean.TRUE);
        }
        return res.fail("A-XXX", "HokageUserDO addSupervisor error");
    }

    @Override
    public ServiceResponse<Boolean> deleteSupervisor(List<Long> ids) {
        checkNotNull(ids, "supervisor ids can't be null");

        ServiceResponse<Boolean> res = new ServiceResponse<>();
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
            return res.success(Boolean.TRUE);
        }
        return res.fail("A-XXX", "HokageUserDO deleteSupervisor error");
    }

    @Override
    public ServiceResponse<Boolean> recycleSupervisor(Long id) {
        checkNotNull(id, "supervisor ids can't be null");

        ServiceResponse<Boolean> res = new ServiceResponse<>();
        boolean isSucceed = supervisorServerDao.removeBySupervisorId(id) > 0;
        if (isSucceed) {
            return res.success(Boolean.TRUE);
        }
        return res.fail("A-XXX", "HokageUserDO recycleSupervisor error");
    }

    @Override
    public ServiceResponse<Boolean> recycleSupervisor(Long id, List<Long> serverIds) {
        checkNotNull(id, "supervisor id can't be null");
        checkNotNull(id, "serverIds can't be null");

        ServiceResponse<Boolean> res = new ServiceResponse<>();
        boolean isSucceed = supervisorServerDao.removeBySupervisorId(id, serverIds) > 0;
        if (isSucceed) {
            return res.success(Boolean.TRUE);
        }
        return res.fail("A-XXX", "HokageUserDO recycleSupervisor error");
    }

    @Override
    public ServiceResponse<Boolean> grantSupervisor(Long id, List<Long> serverIds) {
        checkNotNull(id, "supervisor id can't be null");
        checkNotNull(id, "serverIds can't be null");

        ServiceResponse<Boolean> res = new ServiceResponse<>();
        boolean isSucceed = supervisorServerDao.addBySupervisorId(id, serverIds) > 0;
        if (isSucceed) {
            return res.success(Boolean.TRUE);
        }
        return res.fail("A-XXX", "HokageUserDO grantSupervisor error");
    }

    @Override
    public ServiceResponse<List<HokageUserVO>> listOrdinaryUsers(Long supervisorId) {
        ServiceResponse<List<HokageUserVO>> response = new ServiceResponse<>();
        HokageUserDO userDO = userDao.getUserById(supervisorId);
        List<HokageUserDO> userDOList = new ArrayList<>();
        // if supervisorId is a super, list all ordinary user
        if (userDO.getRole().equals(UserRoleEnum.super_operator.getValue())) {
            userDOList = userDao.listUserByRole(UserRoleEnum.subordinate.getValue());
        } else {
            List<Long> userIdList = supervisorSubordinateDao.listBySupervisorId(supervisorId).stream()
                    .map(HokageSupervisorSubordinateDO::getSubordinateId)
                    .collect(Collectors.toList());
            userDOList = userDao.listUserByIds(userIdList);
        }
        List<HokageUserVO> userVOList = userDOList.stream().map(this::subordinateUserDO2UserVO).collect(Collectors.toList());
        response.success(userVOList);

        return response;
    }

    /**
     * search subordinate
     * @param form
     * @return
     */
    @Override
    public ServiceResponse<List<HokageUserVO>> searchSubordinates(UserServerSearchForm form) {
        ServiceResponse<List<HokageUserVO>> res = new ServiceResponse<>();

        List<HokageUserDO>  supervisorList;
        if (Objects.nonNull(form.getId()) && form.getId() > 0) {
            HokageUserDO userDO = userDao.getUserById(form.getId());
            supervisorList = Collections.singletonList(userDO);
        } else if (StringUtils.isNoneBlank(form.getLabel())) {
            // 1. retrieve server info by label
            List<HokageServerDO> serverDOList = serverDao.listByType(form.getLabel());
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
        res.success(userVOList);

        return res;
    }

    @Override
    public ServiceResponse<Boolean> addSubordinate(Long supervisorId, List<Long> ids) {

        ServiceResponse<Boolean> res = new ServiceResponse<>();

        List<HokageSupervisorSubordinateDO> supervisorSubordinateDOList = ids.stream().map(id -> {
            HokageSupervisorSubordinateDO supervisorSubordinateDO  = new HokageSupervisorSubordinateDO();

            supervisorSubordinateDO.setSupervisorId(supervisorId);
            supervisorSubordinateDO.setSubordinateId(id);

            return supervisorSubordinateDO;
        }).collect(Collectors.toList());

        Long result = supervisorSubordinateDao.insertBatch(supervisorSubordinateDOList);
        if (result > 0) {
            return res.success(Boolean.TRUE);
        }
        return res.fail("A-XXX", "HokageUserDO addSupervisor error");
    }

    @Override
    public ServiceResponse<Boolean> deleteSubordinate(Long supervisorId, List<Long> ids) {

        ServiceResponse<Boolean> res = new ServiceResponse<>();

        Long result = supervisorSubordinateDao.deleteSubordinate(supervisorId, ids);
        if (result > 0) {
            return res.success(Boolean.TRUE);
        }
        return res.fail("A-XXX", "HokageUserDO addSupervisor error");
    }

    @Override
    public ServiceResponse<Boolean> grantSubordinate(Long id, List<Long> serverIds) {
        ServiceResponse<Boolean> res = new ServiceResponse<>();
        boolean isSucceed = subordinateServerDao.addBySubordinateId(id, serverIds) > 0;
        if (isSucceed) {
            return res.success(Boolean.TRUE);
        }
        return res.fail("A-XXX", "HokageUserDO grantSupervisor error");
    }

    @Override
    public ServiceResponse<Boolean> recycleSubordinate(Long id) {
        ServiceResponse<Boolean> res = new ServiceResponse<>();
        boolean isSucceed = subordinateServerDao.removeBySubordinateId(id) > 0;
        if (isSucceed) {
            return res.success(Boolean.TRUE);
        }
        return res.fail("A-XXX", "HokageUserDO recycleSupervisor error");
    }

    @Override
    public ServiceResponse<Boolean> recycleSubordinate(Long id, List<Long> serverIds) {
        ServiceResponse<Boolean> res = new ServiceResponse<>();
        boolean isSucceed = subordinateServerDao.removeBySubordinateId(id, serverIds) > 0;
        if (isSucceed) {
            return res.success(Boolean.TRUE);
        }
        return res.fail("A-XXX", "HokageUserDO recycleSupervisor error");
    }

    @Override
    public ServiceResponse<Integer> getRoleByUserId(Long id) {
        return null;
    }

    @Override
    public ServiceResponse<Boolean> isSuperOperator(Long id) {
        ServiceResponse<Boolean> response = new ServiceResponse<>();
        ServiceResponse<Integer> roleResponse = this.getRoleByUserId(id);

        if (!roleResponse.getSucceeded() || Objects.isNull(roleResponse.getData())) {
            return response.fail(roleResponse.getCode(), roleResponse.getMsg());
        }

        return response.success(UserRoleEnum.super_operator.getValue().equals(roleResponse.getData()));
    }

    @Override
    public ServiceResponse<Boolean> isSupervisor(Long id) {
        ServiceResponse<Boolean> response = new ServiceResponse<>();
        ServiceResponse<Integer> roleResponse = this.getRoleByUserId(id);

        if (!roleResponse.getSucceeded() || Objects.isNull(roleResponse.getData())) {
            return response.fail(roleResponse.getCode(), roleResponse.getMsg());
        }

        return response.success(UserRoleEnum.supervisor.getValue().equals(roleResponse.getData()));
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
