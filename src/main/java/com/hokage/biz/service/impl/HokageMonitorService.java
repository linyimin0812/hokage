package com.hokage.biz.service.impl;

import com.hokage.biz.request.command.BaseCommandParam;
import com.hokage.biz.response.resource.general.AccountInfoVO;
import com.hokage.biz.response.resource.general.BasicInfoVO;
import com.hokage.biz.response.resource.general.GeneralInfoVO;
import com.hokage.biz.response.resource.general.LastLogInfoVO;
import com.hokage.biz.response.resource.network.ArpInfoVo;
import com.hokage.biz.response.resource.network.ConnectionInfoVO;
import com.hokage.biz.response.resource.network.InterfaceIpVO;
import com.hokage.biz.response.resource.network.NetworkInfoVO;
import com.hokage.biz.response.resource.system.DiskInfoVO;
import com.hokage.biz.response.resource.system.ProcessInfoVO;
import com.hokage.biz.response.resource.system.SystemInfoVO;
import com.hokage.biz.service.AbstractCommandService;
import com.hokage.common.ServiceResponse;
import com.hokage.ssh.command.handler.MonitorCommandHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


/**
 * @author yiminlin
 * @date 2021/06/16 9:13 am
 * @description server monitor service implementation
 **/
@Slf4j
@Service
public class HokageMonitorService extends AbstractCommandService {

    private MonitorCommandHandler<BaseCommandParam> commandHandler;

    @Autowired
    public void setCommandHandler(MonitorCommandHandler<BaseCommandParam> commandHandler) {
        this.commandHandler = commandHandler;
    }

    public ServiceResponse<BasicInfoVO> acquireBasic(String serverKey) {
        ServiceResponse<BasicInfoVO> response = new ServiceResponse<>();
        BasicInfoVO basicInfoVO = new BasicInfoVO();

        ServiceResponse<List<GeneralInfoVO>> cpuInfoResult = this.execute(serverKey, null, commandHandler.lsCpuHandler);
        if (cpuInfoResult.getSucceeded()) {
            basicInfoVO.setCpuInfo(cpuInfoResult.getData());
        }

        ServiceResponse<List<GeneralInfoVO>> memInfoResult = this.execute(serverKey, null, commandHandler.memInfoHandler);
        if (memInfoResult.getSucceeded()) {
            basicInfoVO.setMemInfo(memInfoResult.getData());
        }

        ServiceResponse<List<AccountInfoVO>> accountInfoResult = this.execute(serverKey, null, commandHandler.accountInfoHandler);
        if (accountInfoResult.getSucceeded()) {
            basicInfoVO.setAccountInfo(accountInfoResult.getData());
        }

        ServiceResponse<List<LastLogInfoVO>> lastLogResult = this.execute(serverKey, null, commandHandler.lastLogInfoHandler);
        if (lastLogResult.getSucceeded()) {
            basicInfoVO.setLastLogInfo(lastLogResult.getData());
        }

        ServiceResponse<List<GeneralInfoVO>> generalInfoResult = this.execute(serverKey, null, commandHandler.generalInfoHandler);
        if (generalInfoResult.getSucceeded()) {
            basicInfoVO.setGeneralInfo(generalInfoResult.getData());
        }

        return response.success(basicInfoVO);
    }

    public ServiceResponse<SystemInfoVO> acquireSystem(String serverKey) {
        ServiceResponse<SystemInfoVO> response = new ServiceResponse<>();
        SystemInfoVO systemInfoVO = new SystemInfoVO();

        ServiceResponse<List<ProcessInfoVO>> processResult = this.execute(serverKey, null, commandHandler.processHandler);

        if (processResult.getSucceeded()) {
            systemInfoVO.setProcessInfo(processResult.getData());
        }

        ServiceResponse<List<DiskInfoVO>> diskResult = this.execute(serverKey, null, commandHandler.diskPartitionHandler);

        if (diskResult.getSucceeded()) {
            systemInfoVO.setDiskInfo(diskResult.getData());
        }

        return response.success(systemInfoVO);
    }

    public ServiceResponse<NetworkInfoVO> acquireNetwork(String serverKey) {
        ServiceResponse<NetworkInfoVO> response = new ServiceResponse<>();
        NetworkInfoVO networkInfoVO = new NetworkInfoVO();

        ServiceResponse<List<InterfaceIpVO>> interfaceIpResult = this.execute(serverKey, null, commandHandler.interfaceIpHandler);
        if (interfaceIpResult.getSucceeded()) {
            networkInfoVO.setInterfaceIpInfo(interfaceIpResult.getData());
        }

        ServiceResponse<List<ArpInfoVo>> arpResult = this.execute(serverKey, null, commandHandler.arpHandler);
        if (arpResult.getSucceeded()) {
            networkInfoVO.setArpInfo(arpResult.getData());
        }

        ServiceResponse<List<ConnectionInfoVO>> netstatResult = this.execute(serverKey, null, commandHandler.netstatHandler);
        if (netstatResult.getSucceeded()) {
            networkInfoVO.setConnectionInfo(netstatResult.getData());
        }

        return response.success(networkInfoVO);
    }
}
