package com.hokage.persistence.mapper;

import com.hokage.persistence.dataobject.HokageSupervisorServerDO;
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
     * @param supervisorServerDO a record
     * @return row affected
     */
    Long insert(HokageSupervisorServerDO supervisorServerDO);

    /**
     * update a record
     * @param supervisorServerDO a record which need to update
     * @return rows affected
     */
    Long update(HokageSupervisorServerDO supervisorServerDO);

    /**
     * retrieve servers' supervisor by server ids
     * @param ids server is list
     * @return records which id is in ids list
     */
    List<HokageSupervisorServerDO> listByServerIds(List<Long> ids);

    /**
     * retrieve server id by supervisor ids
     * @param ids supervisor id list
     * @return records which supervisor id is in id list
     */
    List<HokageSupervisorServerDO> listBySupervisorIds(List<Long> ids);

    /**
     * recycle server manage right by supervisorId
     * @param id supervisor id
     * @return rows affected
     */
    Integer removeBySupervisorId(@Param("id") Long id);

    /**
     * recycle server manage right by supervisorId and server ids
     * @param id supervisor id
     * @param serverIds server id list
     * @return rows affected
     */
    Integer removeBySupervisorId(@Param("id") Long id, @Param("serverIds") List<Long> serverIds);

    /**
     * grant servers to a supervisor
     * @param id supervisor id
     * @param serverIds server id list
     * @return rows affected
     */
    Integer addBySupervisorId(@Param("supervisorId") Long id, @Param("serverIds") List<Long> serverIds);

    /**
     * retrieve relationship based-on supervisor id and server id
     * @param supervisorId supervisor id
     * @param serverId server id
     * @return record which supervisor id is supervisorId and server id is serverId
     */
    HokageSupervisorServerDO queryBySupervisorIdAndServerId(@Param("supervisorId") Long supervisorId, @Param("serverId") Long serverId);
}
