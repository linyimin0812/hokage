package com.banzhe.hokage.biz.service.impl;

import com.banzhe.hokage.biz.enums.OperationTypeEnum;
import com.banzhe.hokage.biz.enums.SequenceNameEnum;
import com.banzhe.hokage.biz.enums.UserErrorCodeEnum;
import com.banzhe.hokage.biz.enums.UserRoleEnum;
import com.banzhe.hokage.biz.form.user.UserServerSearchForm;
import com.banzhe.hokage.biz.response.HokageOperation;
import com.banzhe.hokage.biz.response.server.HokageServerVO;
import com.banzhe.hokage.biz.response.user.HokageUserVO;
import com.banzhe.hokage.biz.service.HokageSequenceService;
import com.banzhe.hokage.biz.service.HokageUserService;
import com.banzhe.hokage.common.ServiceResponse;
import com.banzhe.hokage.persistence.dao.HokageServerDao;
import com.banzhe.hokage.persistence.dao.HokageSubordinateServerDao;
import com.banzhe.hokage.persistence.dao.HokageSupervisorServerDao;
import com.banzhe.hokage.persistence.dao.HokageUserDao;
import com.banzhe.hokage.persistence.dataobject.HokageServerDO;
import com.banzhe.hokage.persistence.dataobject.HokageSubordinateServerDO;
import com.banzhe.hokage.persistence.dataobject.HokageSupervisorServerDO;
import com.banzhe.hokage.persistence.dataobject.HokageUserDO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServiceResponse<HokageUserDO> register(HokageUserDO hokageUserDO) {

        ServiceResponse<HokageUserDO> res = new ServiceResponse<>();

        // 1. determine whether the mail already exists
        HokageUserDO userDO = userDao.getUserByEmail(hokageUserDO.getEmail());
        if (Objects.nonNull(userDO)) {
            return res.fail(UserErrorCodeEnum.USERNAME_DUPLICATE_ERROR.getCode(), UserErrorCodeEnum.USERNAME_DUPLICATE_ERROR.getMsg());
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
        return res.fail(UserErrorCodeEnum.USER_REGISTER_FAIL.getCode(), UserErrorCodeEnum.USER_REGISTER_FAIL.getMsg());
    }

    @Override
    public ServiceResponse<HokageUserDO> login(HokageUserDO hokageUserDO) {

        ServiceResponse<HokageUserDO> res = new ServiceResponse<>();

        res.fail(UserErrorCodeEnum.USER_PASSWD_ERROR.getCode(), UserErrorCodeEnum.USER_PASSWD_ERROR.getMsg());

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

        List<HokageUserDO> users = userDao.ListUserByRole(UserRoleEnum.supervisor.getValue());

        List<HokageUserVO> userVOList = users.stream().map(this::userDO2UserVO).collect(Collectors.toList());

        res.success(userVOList);

        return res;
    }

    @Override
    public ServiceResponse<List<HokageUserVO>> searchSupervisors(UserServerSearchForm form) {

        ServiceResponse<List<HokageUserVO>> res = new ServiceResponse<>();

        List<HokageUserDO>  supervisorList = Collections.EMPTY_LIST;
        if (Objects.nonNull(form.getId()) && form.getId() > 0) {
            HokageUserDO userDO = userDao.getUserById(form.getId());
            supervisorList = Arrays.asList(userDO);
        } else if (StringUtils.isNoneBlank(form.getLabel())) {
            // 1. retrieve server info by label
            List<HokageServerDO> serverDOList = serverDao.listByType(form.getLabel());
            // 2. retrieve supervisor ids by server ids
            List<Long> serverIds = serverDOList.stream().map(HokageServerDO::getId).collect(Collectors.toList());
            List<HokageSupervisorServerDO> supervisorServerDOS = supervisorServerDao.listByServerIds(serverIds);
            // 3. retrieve supervisor info by supervisor ids
            List<Long> userIds = supervisorServerDOS.stream().map(HokageSupervisorServerDO::getId).collect(Collectors.toList());
            supervisorList = userDao.listUserByIds(userIds);
        } else {
            HokageUserDO hokageUserDO = new HokageUserDO();
            hokageUserDO.setUsername(form.getUsername());
            supervisorList = userDao.listAll(hokageUserDO);
        }

        List<HokageUserVO> userVOList = supervisorList.stream().map(this::userDO2UserVO).collect(Collectors.toList());
        res.success(userVOList);

        return res;
    }

    private HokageUserVO userDO2UserVO(HokageUserDO userDO) {

        checkNotNull(userDO, "userDO can't be null");

        HokageUserVO userVO = new HokageUserVO();

        // supervisor info
        userVO.setId(userDO.getId());
        userVO.setEmail(userDO.getEmail());
        userVO.setRole(userDO.getRole());
        userVO.setUsername(userDO.getUsername());

        // server information which managed by the supervisor
        List<Long> serverIds = supervisorServerDao.listByServerIds(Arrays.asList(userDO.getId())).stream()
                .map(HokageSupervisorServerDO::getServerId).collect(Collectors.toList());

        List<HokageServerVO> serverVOList = serverDao.selectByIds(serverIds).stream().map(serverDO -> {
            HokageServerVO serverVO = new HokageServerVO();

            // server information
            serverVO.setId(serverDO.getId());
            serverVO.setDomain(serverDO.getDomain());
            serverVO.setHostname(serverDO.getHostname());
            serverVO.setLabels(Arrays.asList(serverDO.getLabel().split(",")));

            // TODO: retrieve server status from ssh

            // supervisor info
            serverVO.setSupervisor(userDO.getUsername());
            serverVO.setSupervisorId(userDO.getId());

            // action information
            List<HokageOperation> operations = Arrays.asList(
                    new HokageOperation(OperationTypeEnum.supervisor.name(), "recycle", "/server/recycle")
            );

            // number of users of the server
            List<HokageSubordinateServerDO> subordinateServerDOList = subordinateServerDao.listByServerId(serverDO.getId());
            serverVO.setUserNum(subordinateServerDOList.size());

            return serverVO;
        }).collect(Collectors.toList());

        List<String> serverLabels = serverVOList.stream().flatMap(serverVO -> serverVO.getLabels().stream()).distinct().collect(Collectors.toList());

        userVO.setServerLabel(serverLabels);
        userVO.setServerNum(serverVOList.size());
        userVO.setServerVOList(serverVOList);

        // action info
        List<HokageOperation> operations = Arrays.asList(
                new HokageOperation(OperationTypeEnum.supervisor.name(), "view", "/user/view"),
                new HokageOperation(OperationTypeEnum.supervisor.name(), "addServer", "/server/add"),
                new HokageOperation(OperationTypeEnum.supervisor.name(), "recycleServer", "/server/recycle")
        );

        userVO.setOperationList(operations);

        return userVO;
    }

}
