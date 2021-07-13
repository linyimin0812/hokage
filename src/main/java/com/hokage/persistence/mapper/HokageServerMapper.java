package com.hokage.persistence.mapper;

import com.hokage.biz.request.AllServerQuery;
import com.hokage.biz.request.SubordinateServerQuery;
import com.hokage.biz.request.SupervisorServerQuery;
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
     * retrieve server infos based-on server group
     * @param group
     * @return
     */
    List<HokageServerDO> selectByGroup(String group);


    /**
     * retrieve server infos based-on supervisor id
     * @param supervisorId
     * @return
     */
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
