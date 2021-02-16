package com.hokage.persistence.mapper;

import com.hokage.persistence.dataobject.HokageUserDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/7/26 9:59 下午
 * @email linyimin520812@gmail.com
 * @description 定义用户表操作接口
 */
@Mapper
@Component
public interface HokageUserMapper {

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
     * retrieve user info by id
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