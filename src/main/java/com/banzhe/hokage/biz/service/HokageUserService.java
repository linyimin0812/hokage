package com.banzhe.hokage.biz.service;

import com.banzhe.hokage.biz.form.user.UserServerSearchForm;
import com.banzhe.hokage.biz.response.user.HokageUserVO;
import com.banzhe.hokage.common.ResultVO;
import com.banzhe.hokage.common.ServiceResponse;
import com.banzhe.hokage.persistence.dao.HokageUserDao;
import com.banzhe.hokage.persistence.dataobject.HokageUserDO;

import java.util.List;

/**
 * @author linyimin
 * @date 2020/7/26 9:59 下午
 * @email linyimin520812@gmail.com
 * @description 定义用户表操作接口
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
     * @return
     */
    ServiceResponse<List<HokageUserVO>> searchSupervisors(UserServerSearchForm form);
}