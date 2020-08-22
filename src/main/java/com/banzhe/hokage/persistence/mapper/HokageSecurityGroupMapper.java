package com.banzhe.hokage.persistence.mapper;

import com.banzhe.hokage.persistence.dataobject.HokageSecurityGroupDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/8/22 11:19 上午
 * @email linyimin520812@gmail.com
 * @description 服务器安全组信息
 */
@Mapper
@Component
public interface HokageSecurityGroupMapper {
    /**
     * 插入一条信息记录
     * @param securityGroupDO
     * @return
     */
    Integer insert(HokageSecurityGroupDO securityGroupDO);

    /**
     * 更新服务器安全组信息
     * @param securityGroupDO
     * @return
     */
    Integer update(HokageSecurityGroupDO securityGroupDO);

    /**
     * 获取所有安全组信息
     * @return
     */
    List<HokageSecurityGroupDO> selectAll();
}
