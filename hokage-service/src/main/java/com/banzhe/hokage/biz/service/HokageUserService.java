package com.banzhe.hokage.biz.service;

import com.banzhe.hokage.common.ResultVO;
import com.banzhe.hokage.common.ServiceResponse;
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
     * 用户注册
     * @param hokageUserDO
     * @return
     */
    ServiceResponse<HokageUserDO> register(HokageUserDO hokageUserDO);

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
    HokageUserDO getUserById(Long id);

    /**
     * 根据用户名查找用户信息
     * @param name
     * @return
     */
    List<HokageUserDO> listByName(String name);

    /**
     * 根据角色查找用户信息
     * @param role
     * @return
     */
    List<HokageUserDao> listByRole(Integer role);

    /**
     * 根据其他条件查找用户信息
     * @param hokageUserDO
     * @return
     */
    List<HokageUserDao> listAll(HokageUserDO hokageUserDO);

    HokageUserDO getUserByEmail(String Email);
}