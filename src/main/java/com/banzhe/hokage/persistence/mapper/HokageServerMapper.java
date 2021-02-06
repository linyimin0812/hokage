package com.banzhe.hokage.persistence.mapper;

import com.banzhe.hokage.biz.request.AllServerQuery;
import com.banzhe.hokage.biz.request.SubordinateServerQuery;
import com.banzhe.hokage.biz.request.SupervisorServerQuery;
import com.banzhe.hokage.persistence.dataobject.HokageServerDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/8/22 11:28 am
 * @email linyimin520812@gmail.com
 * @description server info mapper interface
 */
@Mapper
@Component
public interface HokageServerMapper {
    /**
     * insert a new record
     * @param serverDO
     * @return
     */
    Long insert(HokageServerDO serverDO);

    /**
     * update a record
     * @param serverDO
     * @return
     */
    Long update(HokageServerDO serverDO);

    /**
     * retrieve all server info
     * @return
     */
    List<HokageServerDO> selectAll();

    /**
     * retrieve server infos based-on server id
     * @param ids
     * @return
     */
    List<HokageServerDO> selectByIds(List<Long> ids);

    /**
     * retrieve server info based-on server id
     * @param id
     * @return
     */
    HokageServerDO selectById(Long id);

    /**
     * retrieve server info based-on server type
     * @param type
     * @return
     */
    List<HokageServerDO> selectByType(String type);

    /**
     * retrieve server infos based-on server group
     * @param group
     * @return
     */
    List<HokageServerDO> selectByGroup(String group);


    List<HokageServerDO> selectBySupervisorId(Long supervisorId);

    /**
     * query server info
     * @param query
     * @return
     */
    List<HokageServerDO> selectByAllServerQuery(AllServerQuery query);

    /**
     * query server info
     * @param query
     * @return
     */
    List<HokageServerDO> selectBySupervisorServerQuery(SupervisorServerQuery query);

    /**
     * query server info
     * @param query
     * @return
     */
    List<HokageServerDO> selectBySubordinateServerQuery(SubordinateServerQuery query);
}
