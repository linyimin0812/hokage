package com.hokage.biz.service;

import com.hokage.persistence.dataobject.HokageSubordinateServerDO;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/8/22 11:56 上午
 * @email linyimin520812@gmail.com
 * @description
 */
public interface HokageSubordinateServerService {

    /**
     * 插入一条新纪录
     * @param subordinateServerDO
     * @return
     */
    Long insert(HokageSubordinateServerDO subordinateServerDO);

    /**
     * 更新一条记录
     * @param subordinateServerDO
     * @return
     */
    Long update(HokageSubordinateServerDO subordinateServerDO);

    /**
     * 查找服务器的使用者id
     * @param id
     * @return
     */
    List<HokageSubordinateServerDO> selectByServerId(Long id);

    /**
     * 查找使用者id下的服务器id
     * @param id
     * @return
     */
    List<HokageSubordinateServerDO> selectByOrdinateId(Long id);
}
