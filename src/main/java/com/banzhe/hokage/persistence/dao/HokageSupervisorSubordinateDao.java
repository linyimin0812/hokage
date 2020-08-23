package com.banzhe.hokage.persistence.dao;

import com.banzhe.hokage.persistence.dataobject.HokageSupervisorSubordinateDO;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/8/22 11:42 上午
 * @email linyimin520812@gmail.com
 * @description 管理员与用户关系映射表
 */
public interface HokageSupervisorSubordinateDao {

    /**
     * 插入一条新纪录
     * @param supervisorSubordinateDO
     * @return
     */
    Long insert(HokageSupervisorSubordinateDO supervisorSubordinateDO);

    /**
     * 更新一条记录
     * @param supervisorSubordinateDO
     * @return
     */
    Long update(HokageSupervisorSubordinateDO supervisorSubordinateDO);

    /**
     * 根据id查找管理员与用户的关系映射信息
     * @param id
     * @return
     */
    HokageSupervisorSubordinateDO selectById(Long id);

    /**
     * 查找全部信息
     * @return
     */
    List<HokageSupervisorSubordinateDO> selectAll();

    /**
     * 查找管理员名下的用户id
     * @param id
     * @return
     */
    List<HokageSupervisorSubordinateDO> selectBySupervisorId(Long id);

    /**
     * 查找用户对应的管理员id
     * @param id
     * @return
     */
    List<HokageSupervisorSubordinateDO> selectBySubordinateId(Long id);
}
