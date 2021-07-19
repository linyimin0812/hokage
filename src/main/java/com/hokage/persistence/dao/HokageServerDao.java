package com.hokage.persistence.dao;

import com.hokage.biz.request.server.AllServerQuery;
import com.hokage.biz.request.server.SubordinateServerQuery;
import com.hokage.biz.request.server.SupervisorServerQuery;
import com.hokage.persistence.dataobject.HokageServerDO;

import java.util.List;
import java.util.Optional;

/**
 * @author linyimin
 * @date 2020/8/22 11:28 am
 * @email linyimin520812@gmail.com
 * @description hokage server dao interface
 */
public interface HokageServerDao {
    /**
     * insert a new record
     * @param serverDO {@link HokageServerDO}
     * @return rows affected
     */
    Long insert(HokageServerDO serverDO);

    /**
     * update a record
     * @param serverDO {@link HokageServerDO}
     * @return rows affected
     */
    Long update(HokageServerDO serverDO);

    /**
     * retrieve all server
     * @return {@link List<HokageServerDO>}
     */
    List<HokageServerDO> selectAll();

    /**
     * retrieve server based-on batch ids
     * @param ids server id list
     * @return {@link List<HokageServerDO>}
     */
    List<HokageServerDO> selectByIds(List<Long> ids);

    /**
     * retrieve server based-on id
     * @param id server primary id
     * @return {@link HokageServerDO}
     */
    HokageServerDO selectById(Long id);

    /**
     * retrieve servers based-on server type
     * @param type server type
     * @return {@link List<HokageServerDO>}
     */
    List<HokageServerDO> listByType(String type);

    /**
     * retrieve servers based-on server group
     * @param group server group
     * @return {@link List<HokageServerDO>}
     */
    List<HokageServerDO> selectByGroup(String group);

    /**
     * retrieve server based-on supervisor id
     * @param supervisorId supervisor id
     * @return {@link List<HokageServerDO>}
     */
    List<HokageServerDO> selectBySupervisorId(Long supervisorId);

    /**
     * select by ip, ssh port and acccount
     * @param ip server ip
     * @param sshPort ssh port
     * @param account account name
     * @return server data object
     */
    HokageServerDO selectByAccount(String ip, String sshPort, String account);

    /**
     * retrieve server info
     * @param query {@link AllServerQuery}
     * @return {@link List<HokageServerDO>}
     */
    List<HokageServerDO> selectByAllQuery(AllServerQuery query);

    /**
     * retrieve server info
     * @param query {@link SupervisorServerQuery}
     * @return {@link List<HokageServerDO>}
     */
    List<HokageServerDO> selectBySupervisorQuery(SupervisorServerQuery query);


    /**
     * select server list by user id
     * @param userId user primary key
     * @return server list which is used by user id
     */
    List<HokageServerDO> selectByUserId(Long userId);

    /**
     * delete by id
     * @param id server primary id
     * @return row affected
     */
    Long deleteById(Long id);

    /**
     * select subordinate server
     * @param query {@link SubordinateServerQuery}
     * @return List<HokageServerDO>
     */
    List<HokageServerDO> selectSubordinateServer(SubordinateServerQuery query);

    /**
     * select server by id and account
     * @param serverId server id
     * @param account account
     * @return {@link HokageServerDO}
     */
    Optional<HokageServerDO> selectByIdAndAccount(long serverId, String account);
}
