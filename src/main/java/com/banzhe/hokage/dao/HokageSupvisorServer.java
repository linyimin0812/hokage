package com.banzhe.hokage.dao;

import com.banzhe.hokage.dataobject.HokageSubordinateServerDO;
import com.banzhe.hokage.dataobject.HokageSupervisorServerDO;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/8/22 12:01 下午
 * @email linyimin520812@gmail.com
 * @description
 */
public interface HokageSupvisorServer {
    /**
     * 插入一条新纪录
     * @param supervisorServerDO
     * @return
     */
    Integer insert(HokageSupervisorServerDO supervisorServerDO);

    /**
     * 更新一条记录
     * @param supervisorServerDO
     * @return
     */
    Integer update(HokageSupervisorServerDO supervisorServerDO);

    /**
     * 查找服务器的管理者id
     * @param id
     * @return
     */
    List<HokageSupervisorServerDO> selectByServerId(Long id);

    /**
     * 查找管理员id下的服务器id
     * @param id
     * @return
     */
    List<HokageSubordinateServerDO> selectBySupervisorId(Long id);
}
