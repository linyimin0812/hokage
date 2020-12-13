package com.banzhe.hokage.persistence.mapper;

import com.banzhe.hokage.persistence.dataobject.HokageSubordinateServerDO;
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
     * @param subordinateServerDO
     * @return
     */
    Long insert(HokageSubordinateServerDO subordinateServerDO);

    /**
     * update a record
     * @param subordinateServerDO
     * @return
     */
    Long update(HokageSubordinateServerDO subordinateServerDO);

    /**
     * retrieve subordinate ids based on server ids
     * @param ids
     * @return
     */
    List<HokageSubordinateServerDO> selectByServerIds(List<Long> ids);

    /**
     * retrieve server ids based on subordinate ids
     * @param ids
     * @return
     */
    List<HokageSubordinateServerDO> selectByOrdinateIds(List<Long> ids);

    /**
     * retrieve record based on primary key
     * @param id
     * @return
     */
    HokageSubordinateServerDO selectById(Long id);

    /**
     * grant servers to a subordinate
     * @param subordinateId
     * @param serverIds
     * @return
     */
    Long addBySubordinateId(@Param("subordinateId") Long subordinateId, @Param("serverIds") List<Long> serverIds);
}
