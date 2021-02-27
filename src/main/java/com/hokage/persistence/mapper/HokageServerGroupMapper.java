package com.hokage.persistence.mapper;

import com.hokage.common.ServiceResponse;
import com.hokage.persistence.dataobject.HokageServerGroupDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/8/22 11:14 am
 * @email linyimin520812@gmail.com
 * @description server group mapper interface
 */
@Mapper
@Component
public interface HokageServerGroupMapper {
    /**
     * insert a new record
     * @param serverGroupDO
     * @return
     */
    Long insert(HokageServerGroupDO serverGroupDO);

    /**
     * list all server group info
     * @return
     */
    List<HokageServerGroupDO> selectAll();

    /**
     * update server group
     * @param serverGroupDO
     * @return
     */
    Long update(HokageServerGroupDO serverGroupDO);

    /**
     * list server group based-on creator id
     * @param id
     * @return
     */
    List<HokageServerGroupDO> listByCreatorId(Long id);

    /**
     * list server group based-on creator and group name
     * @param creatorId
     * @param name
     * @return
     */
    List<HokageServerGroupDO> listByCreatorIdAndName(@Param("id") Long creatorId, @Param("name") String name);
}
