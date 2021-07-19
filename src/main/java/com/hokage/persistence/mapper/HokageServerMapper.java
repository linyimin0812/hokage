package com.hokage.persistence.mapper;

import com.hokage.biz.request.server.AllServerQuery;
import com.hokage.biz.request.server.SubordinateServerQuery;
import com.hokage.biz.request.server.SupervisorServerQuery;
import com.hokage.persistence.dataobject.HokageServerDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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
     * @param serverDO {@link HokageServerDO}
     * @return rows affected
     */
    Long insert(HokageServerDO serverDO);

    /**
     * update a record
     * @param serverDO serverDO {@link HokageServerDO}
     * @return rows affected
     */
    Long update(HokageServerDO serverDO);

    /**
     * retrieve all server info
     * @return {@link List<HokageServerDO>}
     */
    List<HokageServerDO> selectAll();

    /**
     * retrieve server infos based-on server id
     * @param ids server id list
     * @return {@link List<HokageServerDO>}
     */
    List<HokageServerDO> selectByIds(@Param("serverIds") List<Long> ids);

    /**
     * retrieve server info based-on server id
     * @param id server id
     * @return {@link HokageServerDO}
     */
    HokageServerDO selectById(Long id);

    /**
     * retrieve server infos based-on server group
     * @param group server group
     * @return {@link List<HokageServerDO>}
     */
    List<HokageServerDO> selectByGroup(String group);


    /**
     * retrieve server infos based-on supervisor id
     * @param supervisorId supervisor id
     * @return {@link List<HokageServerDO>}
     */
    List<HokageServerDO> selectBySupervisorId(Long supervisorId);

    /**
     * query server info
     * @param query {@link AllServerQuery}
     * @return {@link List<HokageServerDO>}
     */
    List<HokageServerDO> selectByAllServerQuery(AllServerQuery query);

    /**
     * query server info
     * @param query {@link SupervisorServerQuery}
     * @return {@link List<HokageServerDO>}
     */
    List<HokageServerDO> selectBySupervisorServerQuery(SupervisorServerQuery query);

    /**
     * query server info
     * @param query {@link SubordinateServerQuery}
     * @return {@link List<HokageServerDO>}
     */
    List<HokageServerDO> selectBySubordinateServerQuery(SubordinateServerQuery query);

    /**
     * select user by user id
     * @param userId subordinate id or supervisor id
     * @return server list
     */
    List<HokageServerDO> selectByUserId(@Param("userId") Long userId);

    /**
     * delete by id
     * @param id server primary id
     * @return row affected
     */
    Long deleteById(@Param("id") Long id);

    /**
     * select by ip, ssh port and acccount
     * @param ip server ip
     * @param sshPort ssh port
     * @param account account name
     * @return server data object
     */
    HokageServerDO selectByAccount(@Param("ip") String ip, @Param("sshPort") String sshPort, @Param("account") String account);
}
