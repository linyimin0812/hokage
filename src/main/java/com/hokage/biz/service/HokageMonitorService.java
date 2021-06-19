package com.hokage.biz.service;

import com.hokage.biz.response.resource.general.BasicInfoVO;
import com.hokage.biz.response.resource.system.SystemInfoVO;
import com.hokage.common.ServiceResponse;
import com.hokage.ssh.SshClient;

import java.util.function.Function;

/**
 * @author yiminlin
 * @date 2021/06/16 9:12 am
 * @description server monitor service
 **/
public interface HokageMonitorService {

    /**
     * execute command and encapsulates result
     * @param serverKey ip_sshPort_account
     * @param commandHandler command handler
     * @param <R> type of command result
     * @return command result
     */
    <R> ServiceResponse<R> execute(String serverKey, Function<SshClient, ServiceResponse<R>> commandHandler);

    /**
     * acquire server basic information, includes cpu, memory, account, last login account
     * @param serverKey ip_sshPort_account
     * @return server basic information
     */
    ServiceResponse<BasicInfoVO> acquireBasic(String serverKey);

    /**
     * acquire server system information, includes process and disk
     * @param serverKey ip_sshPort_account
     * @return server system information
     */
    ServiceResponse<SystemInfoVO> acquireSystem(String serverKey);
}
