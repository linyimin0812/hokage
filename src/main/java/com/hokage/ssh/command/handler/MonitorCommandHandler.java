package com.hokage.ssh.command.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hokage.biz.enums.ResultCodeEnum;
import com.hokage.biz.request.command.BaseCommandParam;
import com.hokage.biz.request.command.MonitorParam;
import com.hokage.biz.response.resource.general.AccountInfoVO;
import com.hokage.biz.response.resource.general.GeneralInfoVO;
import com.hokage.biz.response.resource.general.LastLogInfoVO;
import com.hokage.biz.response.resource.network.ArpInfoVo;
import com.hokage.biz.response.resource.network.ConnectionInfoVO;
import com.hokage.biz.response.resource.system.DiskInfoVO;
import com.hokage.biz.response.resource.network.InterfaceIpVO;
import com.hokage.biz.response.resource.system.ProcessInfoVO;
import com.hokage.common.ServiceResponse;
import com.hokage.ssh.SshClient;
import com.hokage.ssh.command.AbstractCommand;
import com.hokage.ssh.command.CommandDispatcher;
import com.hokage.ssh.command.result.*;
import com.hokage.ssh.component.SshExecComponent;
import com.hokage.ssh.domain.DiskPartitionProperty;
import com.hokage.util.FileUtil;
import com.hokage.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.text.ParseException;
import java.util.*;
import java.util.function.BiFunction;
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

    public BiFunction<SshClient, MonitorParam, ServiceResponse<Boolean>> killProcessHandler = ((client, monitorParam) -> {
        ServiceResponse<Boolean> response = new ServiceResponse<>();
        try {
            CommandResult killResult = execComponent.execute(client, AbstractCommand.kill(monitorParam.getPid()));
            if (!killResult.isSuccess()) {
                String errMsg = String.format("existStatus: %s, msg: %s", killResult.getExitStatus(), killResult.getMsg());
                return response.fail(ResultCodeEnum.COMMAND_EXECUTED_FAILED.getCode(), errMsg);
            }

            return response.success(Boolean.TRUE);
        } catch (Exception e) {
            log.error("HokageFileManagementServiceImpl.killProcessHandler failed. err: {}", e.getMessage());
            return response.fail(ResultCodeEnum.COMMAND_EXECUTED_FAILED.getCode(), e.getMessage());
        }
    });

    public BiFunction<SshClient, MonitorParam, ServiceResponse<List<DiskInfoVO>>> diskPartitionHandler = ((client, monitorParam) -> {
        ServiceResponse<List<DiskInfoVO>> response = new ServiceResponse<>();
        try {
            AbstractCommand command = dispatcher.dispatch(client);
            CommandResult diskPartitionResult = execComponent.execute(client, command.df());
            if (!diskPartitionResult.isSuccess()) {
                String errMsg = String.format("existStatus: %s, msg: %s", diskPartitionResult.getExitStatus(), diskPartitionResult.getMsg());
                return response.fail(ResultCodeEnum.COMMAND_EXECUTED_FAILED.getCode(), errMsg);
            }
            List<DiskPartitionProperty> properties = JSON.parseArray(diskPartitionResult.getContent(), DiskPartitionProperty.class);
            List<DiskInfoVO> diskInfoVOList = properties.stream().map(property -> {
                DiskInfoVO infoVO = new DiskInfoVO();
                String size = FileUtil.humanReadable(property.getSize() * 1024);
                String used = FileUtil.humanReadable(property.getUsed() * 1024);
                int lastIndex = StringUtils.lastIndexOf(property.getCapacity(), "%");
                int capacity = Integer.parseInt(StringUtils.substring(property.getCapacity(), 0, lastIndex));
                infoVO.setName(property.getFileSystem()).setSize(size).setUsed(used).setCapacity(capacity).setMounted(property.getMounted());
                return infoVO;
            }).collect(Collectors.toList());
            return response.success(diskInfoVOList);
        } catch (Exception e) {
            log.error("HokageFileManagementServiceImpl.diskPartitionHandler failed. err: {}", e.getMessage());
            return response.fail(ResultCodeEnum.COMMAND_EXECUTED_FAILED.getCode(), e.getMessage());
        }
    });

    public BiFunction<SshClient, MonitorParam, ServiceResponse<List<InterfaceIpVO>>> interfaceIpHandler = ((client, monitorParam) -> {
        ServiceResponse<List<InterfaceIpVO>> response = new ServiceResponse<>();
        try {
            CommandResult interfaceIpResult = execComponent.execute(client, AbstractCommand.interfaceIp());
            if (!interfaceIpResult.isSuccess()) {
                String errMsg = String.format("existStatus: %s, msg: %s", interfaceIpResult.getExitStatus(), interfaceIpResult.getMsg());
                return response.fail(ResultCodeEnum.COMMAND_EXECUTED_FAILED.getCode(), errMsg);
            }
            List<InterfaceIpVO> properties = JSON.parseArray(interfaceIpResult.getContent(), InterfaceIpVO.class);
            return response.success(properties);
        } catch (Exception e) {
            log.error("HokageFileManagementServiceImpl.interfaceIpHandler failed. err: {}", e.getMessage());
            return response.fail(ResultCodeEnum.COMMAND_EXECUTED_FAILED.getCode(), e.getMessage());
        }
    });

    public BiFunction<SshClient, MonitorParam, ServiceResponse<List<ArpInfoVo>>> arpHandler = ((client, monitorParam) -> {
        ServiceResponse<List<ArpInfoVo>> response = new ServiceResponse<>();
        try {
            AbstractCommand command = dispatcher.dispatch(client);
            CommandResult arpResult = execComponent.execute(client, command.arp());
            if (!arpResult.isSuccess()) {
                String errMsg = String.format("existStatus: %s, msg: %s", arpResult.getExitStatus(), arpResult.getMsg());
                return response.fail(ResultCodeEnum.COMMAND_EXECUTED_FAILED.getCode(), errMsg);
            }
            List<ArpInfoVo> properties = JSON.parseArray(arpResult.getContent(), ArpInfoVo.class);
            return response.success(properties);
        } catch (Exception e) {
            log.error("HokageFileManagementServiceImpl.arpHandler failed. err: {}", e.getMessage());
            return response.fail(ResultCodeEnum.COMMAND_EXECUTED_FAILED.getCode(), e.getMessage());
        }
    });

    public BiFunction<SshClient, MonitorParam, ServiceResponse<List<ConnectionInfoVO>>> netstatHandler = ((client, monitorParam) -> {
        ServiceResponse<List<ConnectionInfoVO>> response = new ServiceResponse<>();
        try {
            AbstractCommand command = dispatcher.dispatch(client);
            CommandResult netstatResult = execComponent.execute(client, command.netstat());
            if (!netstatResult.isSuccess()) {
                String errMsg = String.format("existStatus: %s, msg: %s", netstatResult.getExitStatus(), netstatResult.getMsg());
                return response.fail(ResultCodeEnum.COMMAND_EXECUTED_FAILED.getCode(), errMsg);
            }
            List<ConnectionInfoVO> properties = JSON.parseArray(netstatResult.getContent(), ConnectionInfoVO.class);
            return response.success(properties);
        } catch (Exception e) {
            log.error("HokageFileManagementServiceImpl.netstatHandler failed. err: {}", e.getMessage());
            return response.fail(ResultCodeEnum.COMMAND_EXECUTED_FAILED.getCode(), e.getMessage());
        }
    });
}
