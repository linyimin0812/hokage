package com.hokage.ssh.command.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hokage.biz.enums.ResultCodeEnum;
import com.hokage.biz.request.command.BaseCommandParam;
import com.hokage.biz.response.resource.general.AccountInfoVO;
import com.hokage.biz.response.resource.general.GeneralInfoVO;
import com.hokage.biz.response.resource.general.LastLogInfoVO;
import com.hokage.biz.response.resource.system.ProcessInfoVO;
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
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author yiminlin
 * @date 2021/06/19 3:39 pm
 * @description command handler
 **/
@Slf4j
@Component
public class MonitorCommandHandler<T extends BaseCommandParam> {
    private CommandDispatcher dispatcher;
    private SshExecComponent execComponent;

    @Autowired
    public void setDispatcher(CommandDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Autowired
    public void setExecComponent(SshExecComponent execComponent) {
        this.execComponent = execComponent;
    }

    public BiFunction<SshClient, T, ServiceResponse<List<GeneralInfoVO>>> lsCpuHandler = ((client, param) -> {
        ServiceResponse<List<GeneralInfoVO>> response = new ServiceResponse<>();
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
            log.error("HokageFileManagementServiceImpl.lsCpuHandler failed. err: {}", e.getMessage());
            return response.fail(ResultCodeEnum.COMMAND_EXECUTED_FAILED.getCode(), e.getMessage());
        }
    });

    public BiFunction<SshClient, T, ServiceResponse<List<GeneralInfoVO>>> memInfoHandler = ((client, param) -> {
        ServiceResponse<List<GeneralInfoVO>> response = new ServiceResponse<>();
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
            log.error("HokageFileManagementServiceImpl.memInfoHandler failed. err: {}", e.getMessage());
            return response.fail(ResultCodeEnum.COMMAND_EXECUTED_FAILED.getCode(), e.getMessage());
        }
    });

    public BiFunction<SshClient, T, ServiceResponse<List<AccountInfoVO>>>  accountInfoHandler = ((client, param) -> {
        ServiceResponse<List<AccountInfoVO>> response = new ServiceResponse<>();
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
            log.error("HokageFileManagementServiceImpl.accountInfoHandler failed. err: {}", e.getMessage());
            return response.fail(ResultCodeEnum.COMMAND_EXECUTED_FAILED.getCode(), e.getMessage());
        }
    });

    public BiFunction<SshClient, T, ServiceResponse<List<LastLogInfoVO>>> lastLogInfoHandler = ((client, param) -> {
        ServiceResponse<List<LastLogInfoVO>> response = new ServiceResponse<>();
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
            log.error("HokageFileManagementServiceImpl.lastLogInfoHandler failed. err: {}", e.getMessage());
            return response.fail(ResultCodeEnum.COMMAND_EXECUTED_FAILED.getCode(), e.getMessage());
        }
    });

    public BiFunction<SshClient, T, ServiceResponse<List<GeneralInfoVO>>> generalInfoHandler = ((client, param) -> {
        ServiceResponse<List<GeneralInfoVO>> response = new ServiceResponse<>();
        try {
            CommandResult generalInfoResult = execComponent.execute(client, "bash ~/.hokage/json-api.sh general_info");
            if (!generalInfoResult.isSuccess()) {
                String errMsg= String.format("exiStatus: %s, msg: %s", generalInfoResult.getExitStatus(), generalInfoResult.getMsg());
                return response.fail(ResultCodeEnum.COMMAND_EXECUTED_FAILED.getCode(), errMsg);
            }
            Map<String, String> generalInfoMap = JSON.parseObject(generalInfoResult.getContent(), new TypeReference<Map<String, String>>(){});
            List<GeneralInfoVO> cpuInfoList = generalInfoMap.entrySet().stream()
                    .map(entry -> new GeneralInfoVO(entry.getKey(), StringUtils.trim(entry.getValue())))
                    .collect(Collectors.toList());
            return response.success(cpuInfoList);
        } catch (Exception e) {
            log.error("HokageFileManagementServiceImpl.generalInfoHandler failed. err: {}", e.getMessage());
            return response.fail(ResultCodeEnum.COMMAND_EXECUTED_FAILED.getCode(), e.getMessage());
        }
    });

    public BiFunction<SshClient, T, ServiceResponse<List<ProcessInfoVO>>> processHandler = ((client, param) -> {
       ServiceResponse<List<ProcessInfoVO>> response = new ServiceResponse<>();
       try {
           AbstractCommand command = dispatcher.dispatch(client);
           CommandResult processInfoResult = execComponent.execute(client, command.process());
           if (!processInfoResult.isSuccess()) {
               String errMsg = String.format("existStatus: %s, msg: %s", processInfoResult.getExitStatus(), processInfoResult.getMsg());
               return response.fail(ResultCodeEnum.COMMAND_EXECUTED_FAILED.getCode(), errMsg);
           }
           List<ProcessInfoVO> processInfoVOList = JSON.parseArray(processInfoResult.getContent(), ProcessInfoVO.class)
                   .stream()
                   .peek(process -> {
                       try {
                           String dateStr = TimeUtil.format(process.getStarted(), "EEE MMM d HH:mm:ss yyyy", "yyyy-MM-dd HH:mm:ss");
                           process.setStarted(dateStr);
                       } catch (ParseException e) {
                           log.error("format date error: {}", e.getMessage());
                       }
                   }).collect(Collectors.toList());
           return response.success(processInfoVOList);
       } catch (Exception e) {
           log.error("HokageFileManagementServiceImpl.processHandler failed. err: {}", e.getMessage());
           return response.fail(ResultCodeEnum.COMMAND_EXECUTED_FAILED.getCode(), e.getMessage());
       }
    });
}
