package com.banzhe.hokage.biz.service;

import com.banzhe.hokage.biz.response.server.HokageServerVO;
import com.banzhe.hokage.common.ServiceResponse;
import com.banzhe.hokage.persistence.dataobject.HokageServerDO;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/8/22 11:28 am
 * @email linyimin520812@gmail.com
 * @description server service interface
 */
public interface HokageServerService {
    /**
     * 插入一条新记录
     * @param serverDO
     * @return
     */
    ServiceResponse<Long> insert(HokageServerDO serverDO);

    /**
     * 更新一条记录
     * @param serverDO
     * @return
     */
    ServiceResponse<Long> update(HokageServerDO serverDO);

    /**
     * retrieve all server
     * @return
     */
    ServiceResponse<List<HokageServerVO>> selectAll();

    /**
     * 根据id获取批量服务器
     * @param ids
     * @return
     */
    ServiceResponse<List<HokageServerDO>> selectByIds(List<Long> ids);

    /**
     * 根据Id获取服务器信息
     * @param id
     * @return
     */
    ServiceResponse<HokageServerDO> selectById(Long id);

    /**
     * 根据类型查找服务器信息
     * @param type
     * @return
     */
    ServiceResponse<List<HokageServerDO>> selectByType(String type);

    /**
     * 根据服务器组查找服务器信息
     * @param group
     * @return
     */
    ServiceResponse<List<HokageServerDO>> selectByGroup(String group);
}
