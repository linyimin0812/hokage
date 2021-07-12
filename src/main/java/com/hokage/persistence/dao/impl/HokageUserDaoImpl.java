package com.hokage.persistence.dao.impl;

import com.hokage.biz.request.user.SubordinateQuery;
import com.hokage.biz.request.user.SupervisorQuery;
import com.hokage.persistence.dao.HokageUserDao;
import com.hokage.persistence.dataobject.HokageUserDO;
import com.hokage.persistence.mapper.HokageUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @author linyimin
 * @date 2020/7/26 9:59 pm
 * @email linyimin520812@gmail.com
 * @description user dao interface
 */
@Repository
public class HokageUserDaoImpl implements HokageUserDao {

    private HokageUserMapper userMapper;

    @Autowired
    public void setUserMapper(HokageUserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public Long insert(HokageUserDO hokageUserDO) {
        return userMapper.insert(hokageUserDO);
    }

    @Override
    public Long update(HokageUserDO hokageUserDO) {
        return userMapper.update(hokageUserDO);
    }

    @Override
    public HokageUserDO getUserById(Long id) {
        return userMapper.getUserById(id);
    }

    @Override
    public List<HokageUserDO> listUserByName(String name) {
        return userMapper.listUserByName(name);
    }

    @Override
    public List<HokageUserDO> listUserByRole(Integer role) {
        return userMapper.listUserByRole(role);
    }

    @Override
    public List<HokageUserDO> listAll(HokageUserDO hokageUserDO) {
        return userMapper.listAll(hokageUserDO);
    }

    @Override
    public HokageUserDO getUserByEmail(String email) {
        return userMapper.getUserByEmail(email);
    }

    @Override
    public List<HokageUserDO> listUserByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return userMapper.listUserByIds(ids);
    }

    @Override
    public List<HokageUserDO> querySupervisor(SupervisorQuery query) {
        List<HokageUserDO> supervisorList = userMapper.querySupervisor(query);
        if (CollectionUtils.isEmpty(supervisorList)) {
            return Collections.emptyList();
        }
        return supervisorList;
    }

    @Override
    public List<HokageUserDO> querySubordinate(SubordinateQuery query) {
        List<HokageUserDO> subordinateList = userMapper.querySubordinate(query);
        if (CollectionUtils.isEmpty(subordinateList)) {
            return Collections.emptyList();
        }
        return subordinateList;
    }

    @Override
    public HokageUserDO querySupervisorBySubordinateId(Long subordinateId) {
        return userMapper.querySupervisorBySubordinateId(subordinateId);
    }
}