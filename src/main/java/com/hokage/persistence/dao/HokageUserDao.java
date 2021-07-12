package com.hokage.persistence.dao;

import com.hokage.biz.request.UserQuery;
import com.hokage.biz.request.user.SubordinateQuery;
import com.hokage.biz.request.user.SupervisorQuery;
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
     * @param hokageUserDO hokage user data object
     * @return rows affected
     */
    Long insert(HokageUserDO hokageUserDO);

    /**
     * update a record
     * @param hokageUserDO hokage user data object
     * @return rows affected
     */
    Long update(HokageUserDO hokageUserDO);

    /**
     * retrieve user info by user id
     * @param id user primary id
     * @return user which meet the criteria
     */
    HokageUserDO getUserById(Long id);

    /**
     * retrieve user info by username
     * @param name user name
     * @return user which meet the criteria
     */
    List<HokageUserDO> listUserByName(String name);

    /**
     * retrieve user info by role
     * @param role role of user {@link com.hokage.biz.enums.UserRoleEnum}
     * @return a list of user which role is meet the criteria
     */
    List<HokageUserDO> listUserByRole(Integer role);

    /**
     * retrieve user info by hokageUserDO
     * @param hokageUserDO user data object
     * @return a list of user which meet the criteria
     */
    List<HokageUserDO> listAll(HokageUserDO hokageUserDO);

    /**
     * retrieve user info by email
     * @param email email of the user
     * @return the user which meet the criteria
     */
    HokageUserDO getUserByEmail(String email);

    /**
     * list user info by user ids
     * @param ids user id list
     * @return a list of user which meet the criteria
     */
    List<HokageUserDO> listUserByIds(List<Long> ids);

    /**
     * list supervisor user with query condition
     * @param query query condition
     * @return user query result
     */
    List<HokageUserDO> querySupervisor(SupervisorQuery query);

    /**
     * list subordinate user with query condition
     * @param query query condition
     * @return user query result
     */
    List<HokageUserDO> querySubordinate(SubordinateQuery query);

    /**
     * query supervisor based-on subordinate id
     * @param subordinateId subordinate primary id
     * @return user which meet the criteria
     */
    HokageUserDO querySupervisorBySubordinateId(Long subordinateId);
}