package com.hokage.persistence.dao;

import com.hokage.persistence.dataobject.HokageUserDO;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/7/26 9:59pm
 * @email linyimin520812@gmail.com
 * @description define user dao interface
 */
public interface HokageUserDao {

    /**
     * insert a new record
     * @param hokageUserDO
     * @return
     */
    Long insert(HokageUserDO hokageUserDO);

    /**
     * update a record
     * @param hokageUserDO
     * @return
     */
    Long update(HokageUserDO hokageUserDO);

    /**
     * retrieve user info by user id
     * @param id
     * @return
     */
    HokageUserDO getUserById(Long id);

    /**
     * retrieve user info by username
     * @param name
     * @return
     */
    List<HokageUserDO> listUserByName(String name);

    /**
     * retrieve user info by role
     * @param role
     * @return
     */
    List<HokageUserDO> listUserByRole(Integer role);

    /**
     * retrieve user info by hokageUserDO
     * @param hokageUserDO
     * @return
     */
    List<HokageUserDO> listAll(HokageUserDO hokageUserDO);

    /**
     * retrieve user info by email
     * @param email
     * @return
     */
    HokageUserDO getUserByEmail(String email);

    /**
     * list user info by user ids
     * @param ids
     * @return
     */
    List<HokageUserDO> listUserByIds(List<Long> ids);
}