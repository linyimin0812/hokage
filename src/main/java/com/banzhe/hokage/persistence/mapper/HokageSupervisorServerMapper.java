package com.banzhe.hokage.persistence.mapper;

import com.banzhe.hokage.persistence.dataobject.HokageSubordinateServerDO;
import com.banzhe.hokage.persistence.dataobject.HokageSupervisorServerDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/8/22 12:01 下午
 * @email linyimin520812@gmail.com
 * @description
 */
@Mapper
@Component
public interface HokageSupervisorServerMapper {
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
