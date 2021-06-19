package com.hokage.biz.service.impl;

import com.hokage.biz.enums.ResultCodeEnum;
import com.hokage.biz.response.resource.general.AccountInfoVO;
import com.hokage.biz.response.resource.general.BasicInfoVO;
import com.hokage.biz.response.resource.general.GeneralInfoVO;
import com.hokage.biz.response.resource.general.LastLogInfoVO;
import com.hokage.biz.response.resource.system.ProcessInfoVO;
import com.hokage.biz.response.resource.system.SystemInfoVO;
import com.hokage.biz.service.HokageMonitorService;
import com.hokage.cache.HokageServerCacheDao;
import com.hokage.common.ServiceResponse;
import com.hokage.ssh.SshClient;
import com.hokage.ssh.command.handler.MonitorCommandHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author yiminlin
 * @date 2021/06/16 9:13 am
 * @description server monitor service implementation
 **/
@Slf4j
@Service
public class HokageMonitorServiceImpl implements HokageMonitorService {

    private HokageServerCacheDao serverCacheDao;
    private MonitorCommandHandler commandHandler;

    @Autowired
    public void setServerCacheDao(HokageServerCacheDao serverCacheDao) {
        this.serverCacheDao = serverCacheDao;
    }

    @Autowired
    public void setCommandHandler(MonitorCommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public <R> ServiceResponse<R> execute(String serverKey, Function<SshClient, ServiceResponse<R>> commandHandler) {
        ServiceResponse<R> response = new ServiceResponse<>();
        Optional<SshClient> optional = serverCacheDao.getExecClient(serverKey);
        if (!optional.isPresent()) {
            return response.fail(ResultCodeEnum.SERVER_NO_FOUND.getCode(), ResultCodeEnum.SERVER_NO_FOUND.getMsg());
        }

        return commandHandler.apply(optional.get());
    }

    @Override
    public ServiceResponse<BasicInfoVO> acquireBasic(String serverKey) {
        ServiceResponse<BasicInfoVO> response = new ServiceResponse<>();
        BasicInfoVO basicInfoVO = new BasicInfoVO();

        ServiceResponse<List<GeneralInfoVO>> cpuInfoResult = this.execute(serverKey, commandHandler.lsCpuHandler);
        if (cpuInfoResult.getSucceeded()) {
            basicInfoVO.setCpuInfo(cpuInfoResult.getData());
        }

        ServiceResponse<List<GeneralInfoVO>> memInfoResult = this.execute(serverKey, commandHandler.memInfoHandler);
        if (memInfoResult.getSucceeded()) {
            basicInfoVO.setMemInfo(memInfoResult.getData());
        }

        ServiceResponse<List<AccountInfoVO>> accountInfoResult = this.execute(serverKey, commandHandler.accountInfoHandler);
        if (accountInfoResult.getSucceeded()) {
            basicInfoVO.setAccountInfo(accountInfoResult.getData());
        }

        ServiceResponse<List<LastLogInfoVO>> lastLogResult = this.execute(serverKey, commandHandler.lastLogInfoHandler);
        if (lastLogResult.getSucceeded()) {
            basicInfoVO.setLastLogInfo(lastLogResult.getData());
        }

        ServiceResponse<List<GeneralInfoVO>> generalInfoResult = this.execute(serverKey, commandHandler.generalInfoHandler);
        if (generalInfoResult.getSucceeded()) {
            basicInfoVO.setGeneralInfo(generalInfoResult.getData());
        }

        return response.success(basicInfoVO);
    }

    @Override
    public ServiceResponse<SystemInfoVO> acquireSystem(String serverKey) {
        ServiceResponse<SystemInfoVO> response = new ServiceResponse<>();
        SystemInfoVO systemInfoVO = new SystemInfoVO();

        ServiceResponse<List<ProcessInfoVO>> processResult = this.execute(serverKey, commandHandler.processHandler);

        if (processResult.getSucceeded()) {
            systemInfoVO.setProcessInfo(processResult.getData());
        }

        return response.success(systemInfoVO);
    }
}
