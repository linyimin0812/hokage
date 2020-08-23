package com.banzhe.hokage.persistence.mapper;

import com.banzhe.hokage.persistence.dataobject.HokageServerDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/8/22 11:28 上午
 * @email linyimin520812@gmail.com
 * @description 服务器信息
 */
@Mapper
@Component
public interface HokageServerMapper {
    /**
     * 插入一条新记录
     * @param serverDO
     * @return
     */
    Long insert(HokageServerDO serverDO);

    /**
     * 更新一条记录
     * @param serverDO
     * @return
     */
    Long update(HokageServerDO serverDO);

    /**
     * 获取所有服务器信息
     * @return
     */
    List<HokageServerDO> selectAll();

    /**
     * 根据id获取批量服务器
     * @param ids
     * @return
     */
    List<HokageServerDO> selectByIds(List<Long> ids);

    /**
     * 根据Id获取服务器信息
     * @param id
     * @return
     */
    HokageServerDO selectById(Long id);

    /**
     * 根据类型查找服务器信息
     * @param type
     * @return
     */
    List<HokageServerDO> selectByType(Integer type);

    /**
     * 根据服务器组查找服务器信息
     * @param group
     * @return
     */
    List<HokageServerDO> selectByGroup(String group);
}
