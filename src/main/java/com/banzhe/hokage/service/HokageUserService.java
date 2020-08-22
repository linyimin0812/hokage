package com.banzhe.hokage.service;

import com.banzhe.hokage.persistence.dao.HokageUserDao;
import com.banzhe.hokage.persistence.dataobject.HokageUserDO;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/7/26 9:59 下午
 * @email linyimin520812@gmail.com
 * @description 定义用户表操作接口
 */
public interface HokageUserService {

    /**
     * 插入一条新的记录
     * @param hokageUserDO
     * @return
     */
    Integer insert(HokageUserDO hokageUserDO);

    /**
     * 更新一条记录
     * @param hokageUserDO
     * @return
     */
    Integer update(HokageUserDO hokageUserDO);

    /**
     * 根据id查找用户信息
     * @param id
     * @return
     */
    HokageUserDO findById(Long id);

    /**
     * 根据用户名查找用户信息
     * @param name
     * @return
     */
    HokageUserDO findByName(String name);

    /**
     * 根据角色查找用户信息
     * @param role
     * @return
     */
    List<HokageUserDao> findByRole(Integer role);

    /**
     * 根据其他条件查找用户信息
     * @param hokageUserDO
     * @return
     */
    List<HokageUserDao> findAll(HokageUserDO hokageUserDO);
}