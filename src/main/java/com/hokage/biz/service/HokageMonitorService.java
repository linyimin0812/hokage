package com.hokage.biz.service;

import com.hokage.biz.response.resource.AccountInfoVO;
import com.hokage.biz.response.resource.BasicInfoVO;
import com.hokage.biz.response.resource.GeneralInfoVO;
import com.hokage.biz.response.resource.LastLogInfoVO;
import com.hokage.common.ServiceResponse;

import java.util.List;

/**
 * @author yiminlin
 * @date 2021/06/16 9:12 am
 * @description server monitor service
 **/
public interface HokageMonitorService {
    /**
     * acquire server cpu information
     * @param serverKey ip_sshPort_account
     * @return cpu information
     */
    ServiceResponse<List<GeneralInfoVO>> lsCpu(String serverKey);

    /**
     * acquire server memory information
     * @param serverKey ip_sshPort_account
     * @return memory information
     */
    ServiceResponse<List<GeneralInfoVO>> memInfo(String serverKey);

    /**
     * acquire server account information
     * @param serverKey ip_sshPort_account
     * @return account information
     */
    ServiceResponse<List<AccountInfoVO>> accountInfo(String serverKey);

    /**
     * acquire server last login account information
     * @param serverKey ip_sshPort_account
     * @return last login account information
     */
    ServiceResponse<List<LastLogInfoVO>> lastLogInfo(String serverKey);

    /**
     * acquire server basic information, includes cpu, memory, account, last login account
     * @param serverKey ip_sshPort_account
     * @return server basic information
     */
    ServiceResponse<BasicInfoVO> acquireBasic(String serverKey);

}
