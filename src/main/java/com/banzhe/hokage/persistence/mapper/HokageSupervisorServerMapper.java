package com.banzhe.hokage.persistence.mapper;

import com.banzhe.hokage.persistence.dataobject.HokageSubordinateServerDO;
import com.banzhe.hokage.persistence.dataobject.HokageSupervisorServerDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/8/22 12:01 pm
 * @email linyimin520812@gmail.com
 * @description hokage_supervisor_server table mapper interface
 */
@Mapper
@Component
public interface HokageSupervisorServerMapper {
    /**
     * insert a new record
     * @param supervisorServerDO
     * @return
     */
    Long insert(HokageSupervisorServerDO supervisorServerDO);

    /**
     * update a record
     * @param supervisorServerDO
     * @return
     */
    Long update(HokageSupervisorServerDO supervisorServerDO);

    /**
     * retrieve servers' supervisor by server ids
     * @param id
     * @return
     */
    List<HokageSupervisorServerDO> listByServerIds(List<Long> id);

    /**
     * retrieve server id by supervisor ids
     * @param id
     * @return
     */
    List<HokageSupervisorServerDO> listBySupervisorIds(List<Long> id);

    /**
     * recycle server manage right by supervisorId
     * @param id
     * @return
     */
    Integer removeBySupervisorId(@Param("id") Long id);

    /**
     * recycle server manage right by supervisorId and server ids
     * @param id
     * @param serverIds
     * @return
     */
    Integer removeBySupervisorId(@Param("id") Long id, @Param("serverIds") List<Long> serverIds);

    /**
     * grant servers to a supervisor
     * @param id
     * @param serverIds
     * @return
     */
    Integer addBySupervisorId(@Param("supervisorId") Long id, @Param("serverIds") List<Long> serverIds);

    /**
     * retrieve relationship based-on supervisor id and server id
     * @param supervisorId
     * @param serverId
     * @return
     */
    HokageSupervisorServerDO queryBySupervisorIdAndServerId(@Param("supervisorId") Long supervisorId, @Param("serverId") Long serverId);
}
