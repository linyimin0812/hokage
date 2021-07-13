package com.hokage.persistence.mapper;

import com.hokage.biz.response.server.HokageServerVO;
import com.hokage.persistence.dataobject.HokageSubordinateServerDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/8/22 11:56 am
 * @email linyimin520812@gmail.com
 * @description
 */

@Mapper
@Component
public interface HokageSubordinateServerMapper {

    /**
     * insert a new record
     * @param subordinateServerDO subordinate server DO
     * @return rows affected
     */
    Long insert(HokageSubordinateServerDO subordinateServerDO);

    /**
     * update a record
     * @param subordinateServerDO subordinateServerDO subordinate server DO
     * @return rows affected
     */
    Long update(HokageSubordinateServerDO subordinateServerDO);

    /**
     * retrieve subordinate ids based on server ids
     * @param ids server id list
     * @return subordinate server list which meet the criteria
     */
    List<HokageSubordinateServerDO> selectByServerIds(List<Long> ids);

    /**
     * retrieve server ids based on subordinate ids
     * @param ids subordinate id list
     * @return subordinate server list which meet the criteria
     */
    List<HokageSubordinateServerDO> selectByOrdinateIds(List<Long> ids);

    /**
     * retrieve record based on primary key
     * @param id primary id
     * @return subordinate server which meet the criteria
     */
    HokageSubordinateServerDO selectById(Long id);

    /**
     * grant servers to a subordinate
     * @param subordinateId subordinate id
     * @param serverIds server id list
     * @return rows affected
     */
    Long addBySubordinateId(@Param("subordinateId") Long subordinateId, @Param("serverIds") List<Long> serverIds);

    /**
     * recycle server manage right by subordinateId
     * @param id subordinate primary id
     * @return rows affected
     */
    Long removeBySubordinateId(@Param("id") Long id);

    /**
     * recycle server manage right by subordinateId and server ids
     * @param id id subordinate primary id
     * @param serverIds server id list
     * @return rows affected
     */
    Long removeBySubordinateId(@Param("id") Long id, @Param("serverIds") List<Long> serverIds);

    /**
     * retrieve relationship based-on subordinate id and server id
     * @param subordinateId subordinate id
     * @param serverId server id
     * @return subordinate server which meet the criteria
     */
    HokageSubordinateServerDO queryBySubordinateIdAndServerId(Long subordinateId, Long serverId);
}
