package com.banzhe.hokage.persistence.dao;

import com.banzhe.hokage.persistence.dataobject.HokageUserDO;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/7/26 9:59 下午
 * @email linyimin520812@gmail.com
 * @description 定义用户表操作接口
 */
public interface HokageUserDao {

    /**
     * 插入一条新的记录
     * @param hokageUserDO
     * @return
     */
    Long insert(HokageUserDO hokageUserDO);

    /**
     * 更新一条记录
     * @param hokageUserDO
     * @return
     */
    Long update(HokageUserDO hokageUserDO);

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
    List<HokageUserDO> findByRole(Integer role);

    /**
     * 根据其他条件查找用户信息
     * @param hokageUserDO
     * @return
     */
    List<HokageUserDO> findAll(HokageUserDO hokageUserDO);
}