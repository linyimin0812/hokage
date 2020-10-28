package com.banzhe.hokage.biz.service.impl;

import com.banzhe.hokage.biz.enums.SequenceNameEnum;
import com.banzhe.hokage.biz.enums.UserErrorCodeEnum;
import com.banzhe.hokage.biz.enums.UserRoleEnum;
import com.banzhe.hokage.biz.response.user.HokageUserVO;
import com.banzhe.hokage.biz.service.HokageSequenceService;
import com.banzhe.hokage.biz.service.HokageUserService;
import com.banzhe.hokage.common.ServiceResponse;
import com.banzhe.hokage.persistence.dao.HokageServerDao;
import com.banzhe.hokage.persistence.dao.HokageUserDao;
import com.banzhe.hokage.persistence.dataobject.HokageUserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author linyimin
 * @date 2020/8/30 2:18 下午
 * @email linyimin520812@gmail.com
 * @description
 */
@Service
public class HokageUserviceImpl implements HokageUserService {

    private HokageUserDao userDao;
    private BCryptPasswordEncoder passwordEncoder;
    private HokageSequenceService sequenceService;
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServiceResponse<HokageUserDO> register(HokageUserDO hokageUserDO) {

        ServiceResponse res = new ServiceResponse<>();

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

        ServiceResponse res = new ServiceResponse<>();

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
        List<HokageUserDO> users = userDao.ListUserByRole(1);

//        return users.stream().map(user -> {
//
//        }).collect(Collectors.toList());
        return null;
    }


}
