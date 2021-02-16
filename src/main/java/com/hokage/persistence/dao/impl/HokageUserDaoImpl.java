package com.hokage.persistence.dao.impl;

import com.hokage.persistence.dao.HokageUserDao;
import com.hokage.persistence.dataobject.HokageUserDO;
import com.hokage.persistence.mapper.HokageUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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

    /**
     * insert a new record
     * @param hokageUserDO
     * @return
     */
    @Override
    public Long insert(HokageUserDO hokageUserDO) {
        return userMapper.insert(hokageUserDO);
    }

    /**
     * update a record
     * @param hokageUserDO
     * @return
     */
    @Override
    public Long update(HokageUserDO hokageUserDO) {
        return userMapper.update(hokageUserDO);
    }

    /**
     * retrieve user info by id
     * @param id
     * @return
     */
    @Override
    public HokageUserDO getUserById(Long id) {
        return userMapper.getUserById(id);
    }

    /**
     * retrieve user info by username
     * @param name
     * @return
     */
    @Override
    public List<HokageUserDO> listUserByName(String name) {
        return userMapper.listUserByName(name);
    }

    /**
     * retrieve user info by role
     * @param role
     * @return
     */
    @Override
    public List<HokageUserDO> listUserByRole(Integer role) {
        return userMapper.listUserByRole(role);
    }

    /**
     * retrieve user info by email
     * @param hokageUserDO
     * @return
     */
    @Override
    public List<HokageUserDO> listAll(HokageUserDO hokageUserDO) {
        return userMapper.listAll(hokageUserDO);
    }

    @Override
    public HokageUserDO getUserByEmail(String email) {
        return userMapper.getUserByEmail(email);
    }

    /**
     * list user info by user ids
     * @param ids
     * @return
     */
    @Override
    public List<HokageUserDO> listUserByIds(List<Long> ids) {
        return userMapper.listUserByIds(ids);
    }
}