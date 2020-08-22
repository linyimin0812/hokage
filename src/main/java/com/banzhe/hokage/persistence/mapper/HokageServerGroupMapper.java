package com.banzhe.hokage.persistence.mapper;

import com.banzhe.hokage.persistence.dataobject.HokageServerGroupDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/8/22 11:14 上午
 * @email linyimin520812@gmail.com
 * @description 服务器组信息
 */
@Mapper
@Component
public interface HokageServerGroupMapper {
    /**
     * 插入一条新的记录
     * @param serverGroupDO
     * @return
     */
    Integer insert(HokageServerGroupDO serverGroupDO);

    /**
     * 返回所有服务器组信息
     * @return
     */
    List<HokageServerGroupDO> selectAll();

    /**
     * 更新服务器组信息
     * @param serverGroupDO
     * @return
     */
    Integer update(HokageServerGroupDO serverGroupDO);

}
