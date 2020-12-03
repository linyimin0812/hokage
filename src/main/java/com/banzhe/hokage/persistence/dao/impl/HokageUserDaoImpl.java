package com.banzhe.hokage.persistence.dao.impl;

import com.banzhe.hokage.persistence.dao.HokageUserDao;
import com.banzhe.hokage.persistence.dataobject.HokageUserDO;
import com.banzhe.hokage.persistence.mapper.HokageUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/7/26 9:59 下午
 * @email linyimin520812@gmail.com
 * @description 定义用户表操作接口
 */
@Repository
public class HokageUserDaoImpl implements HokageUserDao {

    private HokageUserMapper userMapper;

    @Autowired
    public void setUserMapper(HokageUserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 插入一条新的记录
     * @param hokageUserDO
     * @return
     */
    @Override
    public Long insert(HokageUserDO hokageUserDO) {
        return userMapper.insert(hokageUserDO);
    }

    /**
     * 更新一条记录
     * @param hokageUserDO
     * @return
     */
    @Override
    public Long update(HokageUserDO hokageUserDO) {
        return userMapper.update(hokageUserDO);
    }

    /**
     * 根据id查找用户信息
     * @param id
     * @return
     */
    @Override
    public HokageUserDO getUserById(Long id) {
        return userMapper.getUserById(id);
    }

    /**
     * 根据用户名查找用户信息
     * @param name
     * @return
     */
    @Override
    public List<HokageUserDO> ListUserByName(String name) {
        return userMapper.listUserByName(name);
    }

    /**
     * 根据角色查找用户信息
     * @param role
     * @return
     */
    @Override
    public List<HokageUserDO> ListUserByRole(Integer role) {
        return userMapper.listUserByRole(role);
    }

    /**
     * 根据其他条件查找用户信息
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
}