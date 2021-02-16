package com.hokage.biz.service;

import com.hokage.persistence.dataobject.HokageSubordinateServerDO;
import com.hokage.persistence.dataobject.HokageSupervisorServerDO;

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
    Long insert(HokageSupervisorServerDO supervisorServerDO);

    /**
     * 更新一条记录
     * @param supervisorServerDO
     * @return
     */
    Long update(HokageSupervisorServerDO supervisorServerDO);

    /**
     * 查找服务器的管理者id
     * @param id
     * @return
     */
    List<HokageSupervisorServerDO> listByServerId(Long id);

    /**
     * 查找管理员id下的服务器id
     * @param id
     * @return
     */
    List<HokageSubordinateServerDO> listByIds(List<Long> id);
}
