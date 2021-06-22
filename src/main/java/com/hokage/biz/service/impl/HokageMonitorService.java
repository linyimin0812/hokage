package com.hokage.biz.service.impl;

import com.alibaba.fastjson.JSON;
import com.hokage.biz.enums.ResultCodeEnum;
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
import com.hokage.infra.worker.MetricScheduledThreadPoolWorker;
import com.hokage.infra.worker.MetricThreadPoolWorker;
import com.hokage.persistence.dao.HokageServerMetricDao;
import com.hokage.persistence.dataobject.HokageServerMetricDO;
import com.hokage.ssh.SshClient;
import com.hokage.ssh.command.AbstractCommand;
import com.hokage.ssh.command.handler.MonitorCommandHandler;
import com.hokage.ssh.command.result.*;
import com.hokage.ssh.component.SshExecComponent;
import com.hokage.ssh.enums.MetricTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * @author yiminlin
 * @date 2021/06/16 9:13 am
 * @description server monitor service implementation
 **/
@Slf4j
@Service
public class HokageMonitorService extends AbstractCommandService {

    @Value("${system.metric.need.pull}")
    private boolean needPull;

    private MonitorCommandHandler<BaseCommandParam> commandHandler;
    private SshExecComponent execComponent;
    private HokageServerMetricDao metricDao;

    private MetricScheduledThreadPoolWorker scheduledPoolWorker;
    private MetricThreadPoolWorker metricPoolWorker;

    @Autowired
    public void setExecComponent(SshExecComponent execComponent) {
        this.execComponent = execComponent;
    }

    @Autowired
    public void setMetricDao(HokageServerMetricDao metricDao) {
        this.metricDao = metricDao;
    }

    @Autowired
    public void setCommandHandler(MonitorCommandHandler<BaseCommandParam> commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Autowired
    public void setScheduledPoolWorker(MetricScheduledThreadPoolWorker scheduledPoolWorker) {
        this.scheduledPoolWorker = scheduledPoolWorker;
    }

    @Autowired
    public void setMetricPoolWorker(MetricThreadPoolWorker metricPoolWorker) {
        this.metricPoolWorker = metricPoolWorker;
    }

    @PostConstruct
    public void init() {
        if (!needPull) {
            return;
        }
        scheduledPoolWorker.getScheduledService().scheduleAtFixedRate(() -> {
            Map<String, SshClient> cache = getServerCacheDao().getServer2MetricClient().asMap();
            cache.forEach((key, value) -> {
                metricPoolWorker.getExecutorPool().execute(() -> this.acquireSystemStat(value));
                log.info("acquire system stat: {}", value.getSshContext());
            });
        }, 0, 60, TimeUnit.SECONDS);
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

    public void acquireSystemStat(SshClient client) {
        ServiceResponse<Boolean> response = new ServiceResponse<>();
        try {
            CommandResult systemStatResult = execComponent.execute(client, AbstractCommand.systemStat());
            if (!systemStatResult.isSuccess()) {
                String errMsg = String.format("existStatus: %s, msg: %s", systemStatResult.getExitStatus(), systemStatResult.getMsg());
                response.fail(ResultCodeEnum.COMMAND_EXECUTED_FAILED.getCode(), errMsg);
                return;
            }
            SystemStat systemStat = JSON.parseObject(systemStatResult.getContent(), SystemStat.class);

            long timestamp = System.currentTimeMillis();
            List<HokageServerMetricDO> metricDOList = this.assembleMetricList(systemStat).stream().peek(metric -> {
                metric.setTimestamp(timestamp).setServer(client.getSshContext().getIp());
            }).collect(Collectors.toList());

            Long result = metricDao.batInsert(metricDOList);
            if (result > 0) {
                response.success(Boolean.TRUE);
                return;
            }
            response.fail(ResultCodeEnum.SERVER_SYSTEM_ERROR.getCode(), "insert metrics error");
        } catch (Exception e) {
            log.error("HokageFileManagementServiceImpl.systemStatHandler failed. err: {}", e.getMessage());
            response.fail(ResultCodeEnum.COMMAND_EXECUTED_FAILED.getCode(), e.getMessage());
        }
    }

    private List<HokageServerMetricDO> assembleMetricList(SystemStat systemStat) {

        List<HokageServerMetricDO> list = new ArrayList<>();
        list.addAll(this.assembleUploadMetrics(systemStat.getUploadRate()));
        list.addAll(this.assembleDownloadMetrics(systemStat.getDownloadRate()));
        list.addAll(this.assembleMemoryMetrics(systemStat.getMemStat()));
        list.addAll(this.assembleLoadAvgMetrics(systemStat.getLoadAvg()));
        list.addAll(this.assembleCpuMetrics(systemStat.getPreCpuStat(), systemStat.getCurCpuStat()));

        return list;
    }

    private List<HokageServerMetricDO> assembleUploadMetrics(Map<String, Long> uploadRate) {
        return uploadRate.entrySet().stream().map(entry -> {
            HokageServerMetricDO metricDO = new HokageServerMetricDO();
            metricDO.setType(MetricTypeEnum.upload.getValue())
                    .setName(entry.getKey())
                    .setValue(entry.getValue().doubleValue());
            return metricDO;
        }).collect(Collectors.toList());
    }

    private List<HokageServerMetricDO> assembleDownloadMetrics(Map<String, Long> downloadRate) {
        return downloadRate.entrySet().stream().map(entry -> {
            HokageServerMetricDO metricDO = new HokageServerMetricDO();
            metricDO.setType(MetricTypeEnum.download.getValue())
                    .setName(entry.getKey())
                    .setValue(entry.getValue().doubleValue());
            return metricDO;
        }).collect(Collectors.toList());
    }

    private List<HokageServerMetricDO> assembleLoadAvgMetrics(LoadAverageStat loadAverageStat) {
        return Arrays.asList(
                new HokageServerMetricDO().setType(MetricTypeEnum.loadAverage.getValue())
                        .setName("1_min_avg").setValue(loadAverageStat.getOneMinAverage().doubleValue()),
                new HokageServerMetricDO().setType(MetricTypeEnum.loadAverage.getValue())
                        .setName("5_min_avg").setValue(loadAverageStat.getFiveMinAverage().doubleValue()),
                new HokageServerMetricDO().setType(MetricTypeEnum.loadAverage.getValue())
                        .setName("15_min_avg").setValue(loadAverageStat.getFifteenMinAverage().doubleValue())
        );
    }

    private List<HokageServerMetricDO> assembleMemoryMetrics(MemoryStat memoryStat) {
        HokageServerMetricDO metricDO = new HokageServerMetricDO();
        metricDO.setType(MetricTypeEnum.memory.getValue()).setName("memory").setValue(memoryStat.getUsed() / (1.0 * memoryStat.getTotal()) * 100);
        return Collections.singletonList(metricDO);
    }

    private List<HokageServerMetricDO> assembleCpuMetrics(List<CpuStat> preCpuStat, List<CpuStat> curCpuStat) {
        return IntStream.range(0, preCpuStat.size()).mapToObj(index -> {
            HokageServerMetricDO metricDO = new HokageServerMetricDO();
            CpuStat preStat = preCpuStat.get(index);
            CpuStat curStat = curCpuStat.get(index);
            Long preCpuTotal = preStat.getUser() + preStat.getSystem() + preStat.getNice() + preStat.getIdle() + preStat.getIoWait() + preStat.getIrq() + preStat.getSoftIrq();
            Long curCpuTotal = curStat.getUser() + curStat.getSystem() + curStat.getNice() + curStat.getIdle() + curStat.getIoWait() + curStat.getIrq() + curStat.getSoftIrq();
            Long total = curCpuTotal - preCpuTotal;
            Long idle = curStat.getIdle() - preStat.getIdle();
            double usage = (total - idle) / (1.0 * (curCpuTotal - preCpuTotal)) * 100;

            metricDO.setType(MetricTypeEnum.cpu.getValue()).setName(curStat.getName()).setValue(usage);
            return metricDO;
        }).collect(Collectors.toList());
    }
}
