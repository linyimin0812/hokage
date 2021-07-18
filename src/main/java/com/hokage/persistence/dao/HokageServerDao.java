package com.hokage.persistence.dao;

import com.hokage.biz.request.AllServerQuery;
import com.hokage.biz.request.SubordinateServerQuery;
import com.hokage.biz.request.SupervisorServerQuery;
import com.hokage.persistence.dataobject.HokageServerDO;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/8/22 11:28 am
 * @email linyimin520812@gmail.com
 * @description hokage server dao interface
 */
public interface HokageServerDao {
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
     * retrieve all server
     * @return
     */
    List<HokageServerDO> selectAll();

    /**
     * retrieve server based-on batch ids
     * @param ids
     * @return
     */
    List<HokageServerDO> selectByIds(List<Long> ids);

    /**
     * retrieve server based-on id
     * @param id
     * @return
     */
    HokageServerDO selectById(Long id);

    /**
     * retrieve servers based-on server type
     * @param type
     * @return
     */
    List<HokageServerDO> listByType(String type);

    /**
     * retrieve servers based-on server group
     * @param group
     * @return
     */
    List<HokageServerDO> selectByGroup(String group);

    /**
     * retrieve server based-on supervisor id
     * @param supervisorId
     * @return
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
     * retrieve server info
     * @param query {@link SubordinateServerQuery}
     * @return {@link List<HokageServerDO>}
     */
    List<HokageServerDO> selectByAllQuery(SubordinateServerQuery query);

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

}
