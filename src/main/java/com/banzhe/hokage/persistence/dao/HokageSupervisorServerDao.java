package com.banzhe.hokage.persistence.dao;

import com.banzhe.hokage.persistence.dataobject.HokageSubordinateServerDO;
import com.banzhe.hokage.persistence.dataobject.HokageSupervisorServerDO;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/8/22 12:01 下午
 * @email linyimin520812@gmail.com
 * @description
 */
public interface HokageSupervisorServerDao {
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
     * retrieve supervisor id by server ids
     * @param ids
     * @return
     */
    List<HokageSupervisorServerDO> listByServerIds(List<Long> ids);

    /**
     * 查找管理员id下的服务器id
     * @param id
     * @return
     */
    List<HokageSupervisorServerDO> listBySupervisorIds(List<Long> id);
}
