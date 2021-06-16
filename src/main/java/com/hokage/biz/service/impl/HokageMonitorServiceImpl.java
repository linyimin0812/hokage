package com.hokage.biz.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hokage.biz.enums.ResultCodeEnum;
import com.hokage.biz.response.resource.AccountInfoVO;
import com.hokage.biz.response.resource.BasicInfoVO;
import com.hokage.biz.response.resource.GeneralInfoVO;
import com.hokage.biz.response.resource.LastLogInfoVO;
import com.hokage.biz.service.HokageMonitorService;
import com.hokage.cache.HokageServerCacheDao;
import com.hokage.common.ServiceResponse;
import com.hokage.ssh.SshClient;
import com.hokage.ssh.command.AbstractCommand;
import com.hokage.ssh.command.CommandDispatcher;
import com.hokage.ssh.command.CommandResult;
import com.hokage.ssh.component.SshExecComponent;
import com.hokage.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author yiminlin
 * @date 2021/06/16 9:13 am
 * @description server monitor service implementation
 **/
@Slf4j
@Service
public class HokageMonitorServiceImpl implements HokageMonitorService {

    private HokageServerCacheDao serverCacheDao;
    private CommandDispatcher dispatcher;
    private SshExecComponent execComponent;

    @Autowired
    public void setServerCacheDao(HokageServerCacheDao serverCacheDao) {
        this.serverCacheDao = serverCacheDao;
    }

    @Autowired
    public void setDispatcher(CommandDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Autowired
    public void setExecComponent(SshExecComponent execComponent) {
        this.execComponent = execComponent;
    }

    @Override
    public ServiceResponse<List<GeneralInfoVO>> lsCpu(String serverKey) {
        ServiceResponse<List<GeneralInfoVO>> response = new ServiceResponse<>();
        Optional<SshClient> optional = serverCacheDao.getExecClient(serverKey);
        if (!optional.isPresent()) {
            return response.fail(ResultCodeEnum.SERVER_NO_FOUND.getCode(), ResultCodeEnum.SERVER_NO_FOUND.getMsg());
        }
        SshClient client = optional.get();
        try {
            AbstractCommand command = dispatcher.dispatch(client);
            CommandResult lsCpuResult = execComponent.execute(client, command.cpuInfo());
            if (!lsCpuResult.isSuccess()) {
                String errMsg= String.format("exiStatus: %s, msg: %s", lsCpuResult.getExitStatus(), lsCpuResult.getMsg());
                return response.fail(ResultCodeEnum.COMMAND_EXECUTED_FAILED.getCode(), errMsg);
            }

            Map<String, String> cpuInfoMap = JSON.parseObject(lsCpuResult.getContent(), new TypeReference<Map<String, String>>(){});
            List<GeneralInfoVO> cpuInfoList = cpuInfoMap.entrySet().stream()
                    .map(entry -> new GeneralInfoVO(entry.getKey(), StringUtils.trim(entry.getValue())))
                    .collect(Collectors.toList());

            return response.success(cpuInfoList);
        } catch (Exception e) {
            log.error("HokageFileManagementServiceImpl.lscpu failed. err: {}", e.getMessage());
            return response.fail(ResultCodeEnum.COMMAND_EXECUTED_FAILED.getCode(), e.getMessage());
        }
    }

    @Override
    public ServiceResponse<List<GeneralInfoVO>> memInfo(String serverKey) {
        ServiceResponse<List<GeneralInfoVO>> response = new ServiceResponse<>();
        Optional<SshClient> optional = serverCacheDao.getExecClient(serverKey);
        if (!optional.isPresent()) {
            return response.fail(ResultCodeEnum.SERVER_NO_FOUND.getCode(), ResultCodeEnum.SERVER_NO_FOUND.getMsg());
        }
        SshClient client = optional.get();
        try {
            AbstractCommand command = dispatcher.dispatch(client);
            CommandResult memInfoResult = execComponent.execute(client, command.memInfo());
            if (!memInfoResult.isSuccess()) {
                String errMsg= String.format("exiStatus: %s, msg: %s", memInfoResult.getExitStatus(), memInfoResult.getMsg());
                return response.fail(ResultCodeEnum.COMMAND_EXECUTED_FAILED.getCode(), errMsg);
            }

            Map<String, String> cpuInfoMap = JSON.parseObject(memInfoResult.getContent(), new TypeReference<Map<String, String>>(){});
            List<GeneralInfoVO> cpuInfoList = cpuInfoMap.entrySet().stream()
                    .map(entry -> new GeneralInfoVO(entry.getKey(), StringUtils.trim(entry.getValue())))
                    .collect(Collectors.toList());

            return response.success(cpuInfoList);
        } catch (Exception e) {
            log.error("HokageFileManagementServiceImpl.memInfo failed. err: {}", e.getMessage());
            return response.fail(ResultCodeEnum.COMMAND_EXECUTED_FAILED.getCode(), e.getMessage());
        }
    }

    @Override
    public ServiceResponse<List<AccountInfoVO>> accountInfo(String serverKey) {
        ServiceResponse<List<AccountInfoVO>> response = new ServiceResponse<>();
        Optional<SshClient> optional = serverCacheDao.getExecClient(serverKey);
        if (!optional.isPresent()) {
            return response.fail(ResultCodeEnum.SERVER_NO_FOUND.getCode(), ResultCodeEnum.SERVER_NO_FOUND.getMsg());
        }
        SshClient client = optional.get();
        try {
            CommandResult accountInfoResult = execComponent.execute(client, AbstractCommand.accountInfo());
            if (!accountInfoResult.isSuccess()) {
                String errMsg= String.format("exiStatus: %s, msg: %s", accountInfoResult.getExitStatus(), accountInfoResult.getMsg());
                return response.fail(ResultCodeEnum.COMMAND_EXECUTED_FAILED.getCode(), errMsg);
            }

            List<AccountInfoVO> accountInfoVOList = JSON.parseArray(accountInfoResult.getContent(), AccountInfoVO.class);

            // sort by root, user and admin
            List<String> sorts = Arrays.asList("root", "user", "admin");

            accountInfoVOList = accountInfoVOList.stream().sorted((a1, a2) -> {
                int index1 = Integer.MAX_VALUE;
                int index2 = Integer.MAX_VALUE;
                for (String sort : sorts) {
                    if (StringUtils.equals(sort, a1.getAccount())) {
                        index1 = sorts.indexOf(sort);
                    }
                    if (StringUtils.equals(sort, a2.getAccount())) {
                        index2 = sorts.indexOf(sort);
                    }
                }
                return index1 - index2;
            }).collect(Collectors.toList());


            return response.success(accountInfoVOList);
        } catch (Exception e) {
            log.error("HokageFileManagementServiceImpl.accountInfo failed. err: {}", e.getMessage());
            return response.fail(ResultCodeEnum.COMMAND_EXECUTED_FAILED.getCode(), e.getMessage());
        }
    }

    @Override
    public ServiceResponse<List<LastLogInfoVO>> lastLogInfo(String serverKey) {
        ServiceResponse<List<LastLogInfoVO>> response = new ServiceResponse<>();
        Optional<SshClient> optional = serverCacheDao.getExecClient(serverKey);
        if (!optional.isPresent()) {
            return response.fail(ResultCodeEnum.SERVER_NO_FOUND.getCode(), ResultCodeEnum.SERVER_NO_FOUND.getMsg());
        }
        SshClient client = optional.get();
        try {
            AbstractCommand command = dispatcher.dispatch(client);
            CommandResult lastLogResult = execComponent.execute(client, command.lastLog());
            if (!lastLogResult.isSuccess()) {
                String errMsg= String.format("exiStatus: %s, msg: %s", lastLogResult.getExitStatus(), lastLogResult.getMsg());
                return response.fail(ResultCodeEnum.COMMAND_EXECUTED_FAILED.getCode(), errMsg);
            }

            List<LastLogInfoVO> lastLogInfoVOList = JSON.parseArray(lastLogResult.getContent(), LastLogInfoVO.class);

            lastLogInfoVOList = lastLogInfoVOList.stream().peek(lastLogInfoVO -> {
                String dateStr = "";
                try {
                    dateStr = TimeUtil.format(lastLogInfoVO.getLatest(), "EEE MMM dd HH:mm:ss zzz yyyy", "yyyy-MM-dd HH:mm:ss");
                    lastLogInfoVO.setLatest(dateStr);
                } catch (ParseException e) {
                    log.error("format date error: {}", e.getMessage());
                }
            }).collect(Collectors.toList());

            return response.success(lastLogInfoVOList);
        } catch (Exception e) {
            log.error("HokageFileManagementServiceImpl.lastLogInfo failed. err: {}", e.getMessage());
            return response.fail(ResultCodeEnum.COMMAND_EXECUTED_FAILED.getCode(), e.getMessage());
        }
    }

    @Override
    public ServiceResponse<BasicInfoVO> acquireBasic(String serverKey) {
        ServiceResponse<BasicInfoVO> response = new ServiceResponse<>();
        BasicInfoVO basicInfoVO = new BasicInfoVO();

        ServiceResponse<List<GeneralInfoVO>> cpuInfoResult = this.lsCpu(serverKey);
        if (cpuInfoResult.getSucceeded()) {
            basicInfoVO.setCpuInfo(cpuInfoResult.getData());
        }

        ServiceResponse<List<GeneralInfoVO>> memInfoResult = this.memInfo(serverKey);
        if (memInfoResult.getSucceeded()) {
            basicInfoVO.setMemInfo(memInfoResult.getData());
        }

        ServiceResponse<List<AccountInfoVO>> accountInfoResult = this.accountInfo(serverKey);
        if (accountInfoResult.getSucceeded()) {
            basicInfoVO.setAccountInfo(accountInfoResult.getData());
        }

        ServiceResponse<List<LastLogInfoVO>> lastLogResult = this.lastLogInfo(serverKey);
        if (lastLogResult.getSucceeded()) {
            basicInfoVO.setLastLogInfo(lastLogResult.getData());
        }

        return response.success(basicInfoVO);
    }
}
