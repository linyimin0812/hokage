package com.banzhe.hokage.biz.service.impl;

import com.banzhe.hokage.biz.enums.OperationTypeEnum;
import com.banzhe.hokage.biz.enums.SequenceNameEnum;
import com.banzhe.hokage.biz.enums.UserErrorCodeEnum;
import com.banzhe.hokage.biz.enums.UserRoleEnum;
import com.banzhe.hokage.biz.response.HokageOperation;
import com.banzhe.hokage.biz.response.server.HokageServerVO;
import com.banzhe.hokage.biz.response.user.HokageUserVO;
import com.banzhe.hokage.biz.service.HokageSequenceService;
import com.banzhe.hokage.biz.service.HokageUserService;
import com.banzhe.hokage.common.ServiceResponse;
import com.banzhe.hokage.persistence.dao.HokageServerDao;
import com.banzhe.hokage.persistence.dao.HokageSupervisorServerDao;
import com.banzhe.hokage.persistence.dao.HokageUserDao;
import com.banzhe.hokage.persistence.dataobject.HokageSupervisorServerDO;
import com.banzhe.hokage.persistence.dataobject.HokageUserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author linyimin
 * @date: 2020/8/30 2:18 下午
 * @email linyimin520812@gmail.com
 * @description
 */
@Service
public class HokageUserviceImpl implements HokageUserService {

    private HokageUserDao userDao;
    private BCryptPasswordEncoder passwordEncoder;
    private HokageSequenceService sequenceService;
    private HokageSupervisorServerDao supervisorServerDao;
    private HokageServerDao serverDao;

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
    public void setServerDao(HokageServerDao serverDao) {
        this.serverDao = serverDao;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServiceResponse<HokageUserDO> register(HokageUserDO hokageUserDO) {

        ServiceResponse<HokageUserDO> res = new ServiceResponse<>();

        // 1. 先判断邮箱是否已经存在
        HokageUserDO userDO = userDao.getUserByEmail(hokageUserDO.getEmail());
        if (Objects.nonNull(userDO)) {
            return res.fail(UserErrorCodeEnum.USERNAME_DUPLICATE_ERROR.getCode(), UserErrorCodeEnum.USERNAME_DUPLICATE_ERROR.getMsg());
        }
        // 2. 对密码进行加密
        hokageUserDO.setPasswd(passwordEncoder.encode(hokageUserDO.getPasswd()));
        // 3. 用户已注册默认是普通用户
        hokageUserDO.setRole(UserRoleEnum.subordinate.getValue());

        // 获取用户id
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

        // 1. 先判断邮箱是否已经存在
        HokageUserDO userDO = userDao.getUserByEmail(hokageUserDO.getEmail());
        if (Objects.isNull(userDO)) {
            return res;
        }

        // 密码校验
        boolean isMatch = passwordEncoder.matches(hokageUserDO.getPasswd(), userDO.getPasswd());

        return isMatch ? res.success(userDO) : res;
    }

    @Override
    public ServiceResponse<List<HokageUserVO>> listSupervisors() {

        ServiceResponse<List<HokageUserVO>> res = new ServiceResponse<>();

        List<HokageUserDO> users = userDao.ListUserByRole(UserRoleEnum.supervisor.getValue());

        List<HokageUserVO> userVOList = users.stream().map(user -> {

            HokageUserVO userVO = new HokageUserVO();

            userVO.setId(user.getId());
            userVO.setEmail(user.getEmail());
            userVO.setRole(user.getRole());
            userVO.setUsername(user.getUsername());

            // 获取管理员管理的服务器信息
            List<Long> serverIds = supervisorServerDao.listByServerId(user.getId()).stream()
                    .map(HokageSupervisorServerDO::getServerId).collect(Collectors.toList());

            List<HokageServerVO> serverVOList = serverDao.selectByIds(serverIds).stream().map(serverDO -> {
                HokageServerVO serverVO = new HokageServerVO();

                // 服务器信息
                serverVO.setId(serverDO.getId());
                serverVO.setDomain(serverDO.getDomain());
                serverVO.setHostname(serverDO.getHostname());
                serverVO.setLabels(Arrays.asList(serverDO.getType().split(",")));

                // TODO: 从ssh获取服务状态

                // 管理员信息
                serverVO.setSupervisor(user.getUsername());
                serverVO.setSupervisorId(user.getId());

                // 操作信息
                List<HokageOperation> operations = Arrays.asList(
                    new HokageOperation(OperationTypeEnum.supervisor.name(), "")
                );

                return serverVO;
            }).collect(Collectors.toList());

            List<String> serverLabels = serverVOList.stream().flatMap(serverVO -> serverVO.getLabels().stream()).distinct().collect(Collectors.toList());

            userVO.setServerLabel(serverLabels);
            userVO.setServerNum(serverVOList.size());
            userVO.setServerVOList(serverVOList);

            return userVO;

        }).collect(Collectors.toList());

        res.success(userVOList);

        return res;
    }

}
