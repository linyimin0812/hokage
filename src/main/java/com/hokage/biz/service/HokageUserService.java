package com.hokage.biz.service;

import com.hokage.biz.request.server.SubordinateServerQuery;
import com.hokage.biz.request.user.SubordinateQuery;
import com.hokage.biz.request.user.SupervisorQuery;
import com.hokage.biz.response.server.HokageServerVO;
import com.hokage.biz.response.user.HokageUserVO;
import com.hokage.common.ServiceResponse;
import com.hokage.persistence.dataobject.HokageUserDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/7/26 9:59 pm
 * @email linyimin520812@gmail.com
 * @description define user service interface
 */
public interface HokageUserService {

    /**
     * user register
     * @param hokageUserDO hokage user data object
     * @return hokage user data object
     */
    ServiceResponse<HokageUserDO> register(HokageUserDO hokageUserDO);

    /**
     * user login
     * @param hokageUserDO hokage user data object
     * @return hokage user data object
     */
    ServiceResponse<HokageUserDO> login(HokageUserDO hokageUserDO);

    /**
     * search user
     * @param query search condition
     * @return hokage user list which meet the criteria
     */
    ServiceResponse<List<HokageUserVO>> searchSubordinate(SubordinateQuery query);

    /**
     * search supervisor
     * @param query search condition
     * @return supervisor list which are meeting the criteria
     */
    ServiceResponse<List<HokageUserVO>> searchSupervisor(SupervisorQuery query);

    /**
     * add supervisor
     * @param ids supervisor id list
     * @return true: operate success, false: operate failed
     */
    ServiceResponse<Boolean> addSupervisor(List<Long> ids);

    /**
     * remove supervisor
     * @param id supervisor id
     * @return true: delete success, false: delete failed
     */
    ServiceResponse<Boolean> deleteSupervisor(Long id);

    /**
     * recycle all supervisor right by supervisor id
     * @param id supervisor id
     * @return true: recycle success, false: recycle failed
     */
    ServiceResponse<Boolean> recycleSupervisor(Long id);

    /**
     * recycle all supervisor right by supervisor id and server ids
     * @param id supervisor id
     * @param serverIds server id list
     * @return true: recycle supervisor success, false: recycle supervisor failed
     */
    ServiceResponse<Boolean> recycleSupervisor(Long id, List<Long> serverIds);

    /**
     * grant server to supervisor
     * @param id supervisor id
     * @param serverIds server id list
     * @return true: grant supervisor success, false: grant supervisor failed
     */
    ServiceResponse<Boolean> grantSupervisor(Long id, List<Long> serverIds);

    /**
     * list all ordinary user by supervisor, if supervisorId is a super, list all ordinary user
     * @param supervisorId supervisor id
     * @return subordinate user list which meet the criteria
     */
    ServiceResponse<List<HokageUserVO>> listOrdinaryUsers(Long supervisorId);

    /**
     * list all ordinary user by supervisor, if supervisorId is a super, list all ordinary user
     * @return all subordinate user
     */
    ServiceResponse<List<HokageUserVO>> listAllOrdinateUsers();

    /**
     * add subordinate
     * @param supervisorId supervisor id
     * @param ids subordinate id list
     * @return true: success, false: failed
     */
    ServiceResponse<Boolean> addSubordinates2Supervisor(Long supervisorId, List<Long> ids);

    /**
     * delete subordinate
     * @param supervisorId supervisor id
     * @param ids subordinate id list
     * @return true: delete subordinate success, false: delete subordinate failed
     */
    ServiceResponse<Boolean> deleteSubordinate(@Param("supervisorId") Long supervisorId, @Param("subordinateIds") List<Long> ids);

    /**
     * grant server to subordinate
     * @param id user id
     * @param serverIds server id list
     * @return true: grant server success, grant server failed
     */
    ServiceResponse<Boolean> grantServer2Subordinate(Long id, List<Long> serverIds);

    /**
     * recycle all subordinate right by subordinate id
     * @param id subordinate id
     * @return true: recycle subordinate success, recycle subordinate failed
     */
    ServiceResponse<Boolean> recycleSubordinate(Long id);

    /**
     * recycle all subordinate right by supervisor id and server ids
     * @param id subordinate id
     * @param serverIds server id list
     * @return true: recycle subordinate server success, false: recycle failed
     */
    ServiceResponse<Boolean> recycleSubordinate(Long id, List<Long> serverIds);

    /**
     * retrieve user role based on user id
     * @param id user id
     * @return role of the user
     */
    ServiceResponse<Integer> getRoleByUserId(Long id);

    /**
     * determine whether the user is a super
     * @param id: user id
     * @return true: yes, false: no
     */
    ServiceResponse<Boolean> isSuperOperator(Long id);

    /**
     * determine whether the user is a supervisor
     * @param id: user id
     * @return true: yes, false: no
     */
    ServiceResponse<Boolean> isSupervisor(Long id);

    /**
     * delete subordinate's supervisor
     * @param supervisorId supervisor id
     * @param userIds subordinate list
     * @return true: success, false: failed
     */
    ServiceResponse<Boolean> recycleSubordinates2Supervisor(Long supervisorId, List<Long> userIds);

    /**
     * search subordinate server
     * @param query subordinate server query
     * @return server vo list which meet the criteria
     */
    ServiceResponse<List<HokageServerVO>> searchSubordinateServer(SubordinateServerQuery query);
}