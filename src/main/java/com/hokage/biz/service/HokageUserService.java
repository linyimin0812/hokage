package com.hokage.biz.service;

import com.hokage.biz.form.user.UserServerSearchForm;
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
     * @param hokageUserDO
     * @return
     */
    ServiceResponse<HokageUserDO> register(HokageUserDO hokageUserDO);

    /**
     * user login
     * @param hokageUserDO
     * @return
     */
    ServiceResponse<HokageUserDO> login(HokageUserDO hokageUserDO);

    /**
     * retrieve all supervisor
     * @return
     */
    ServiceResponse<List<HokageUserVO>> listSupervisors();

    /**
     * search supervisor
     * @param form
     * @return
     */
    ServiceResponse<List<HokageUserVO>> searchSupervisors(UserServerSearchForm form);

    /**
     * add supervisor
     * @param ids
     * @return
     */
    ServiceResponse<Boolean> addSupervisor(List<Long> ids);

    /**
     * remove supervisor
     * @param ids
     * @return
     */
    ServiceResponse<Boolean> deleteSupervisor(List<Long> ids);

    /**
     * recycle all supervisor right by supervisor id
     * @param id
     * @return
     */
    ServiceResponse<Boolean> recycleSupervisor(Long id);

    /**
     * recycle all supervisor right by supervisor id and server ids
     * @param id
     * @param serverIds
     * @return
     */
    ServiceResponse<Boolean> recycleSupervisor(Long id, List<Long> serverIds);

    /**
     * grant server to supervisor
     * @param id
     * @param serverIds
     * @return
     */
    ServiceResponse<Boolean> grantSupervisor(Long id, List<Long> serverIds);

    /**
     * list all ordinary user by supervisor, if supervisorId is a super, list all ordinary user
     * @param supervisorId
     * @return
     */
    ServiceResponse<List<HokageUserVO>> listOrdinaryUsers(Long supervisorId);

    /**
     * search subordinate
     * @param form
     * @return
     */
    ServiceResponse<List<HokageUserVO>> searchSubordinates(UserServerSearchForm form);

    /**
     * add subordinate
     * @param supervisorId
     * @param ids
     * @return
     */
    ServiceResponse<Boolean> addSubordinate(@Param("supervisorId") Long supervisorId, @Param("subordinateIds") List<Long> ids);

    /**
     * delete subordinate
     * @param supervisorId
     * @param ids
     * @return
     */
    ServiceResponse<Boolean> deleteSubordinate(@Param("supervisorId") Long supervisorId, @Param("subordinateIds") List<Long> ids);

    /**
     * grant server to subordinate
     * @param id
     * @param serverIds
     * @return
     */
    ServiceResponse<Boolean> grantSubordinate(Long id, List<Long> serverIds);

    /**
     * recycle all subordinate right by subordinate id
     * @param id
     * @return
     */
    ServiceResponse<Boolean> recycleSubordinate(Long id);

    /**
     * recycle all subordinate right by supervisor id and server ids
     * @param id
     * @param serverIds
     * @return
     */
    ServiceResponse<Boolean> recycleSubordinate(Long id, List<Long> serverIds);

    /**
     * retrieve user role based on user id
     * @param id
     * @return
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
}