package com.hokage.infra.listener;

import com.alibaba.fastjson.JSON;
import com.hokage.biz.Constant;
import com.hokage.cache.HokageServerCacheDao;
import com.hokage.infra.worker.MasterThreadPoolWorker;
import com.hokage.infra.worker.ScheduledThreadPoolWorker;
import com.hokage.persistence.dao.HokageServerReportHandlerDao;
import com.hokage.persistence.dataobject.HokageServerReportInfoHandlerDO;
import com.hokage.util.IpAddressUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author yiminlin
 * @date 2021/07/05 11:15 pm
 * @description tomcat started listener
 **/
@Slf4j
@Component
public class ApplicationStartedListener implements ApplicationListener<AvailabilityChangeEvent<ReadinessState>> {

    private static final String HEALTH_CHECK = "/actuator/health";

    @Value("${system.report.info.handler}")
    private boolean canBeMaster;

    @Value("${system.report.try.be.master.interval}")
    private Long interval;

    private ServletWebServerApplicationContext context;
    private HokageServerReportHandlerDao reportHandlerDao;
    private ScheduledThreadPoolWorker scheduledPoolWorker;
    private MasterThreadPoolWorker masterPoolWorker;

    private HokageServerCacheDao serverCacheDao;

    @Autowired
    public void setContext(ServletWebServerApplicationContext context) {
        this.context = context;
    }

    @Autowired
    public void setReportHandlerDao(HokageServerReportHandlerDao reportHandlerDao) {
        this.reportHandlerDao = reportHandlerDao;
    }

    @Autowired
    public void setScheduledPoolWorker(ScheduledThreadPoolWorker scheduledPoolWorker) {
        this.scheduledPoolWorker = scheduledPoolWorker;
    }

    @Autowired
    public void setMasterPoolWorker(MasterThreadPoolWorker masterPoolWorker) {
        this.masterPoolWorker = masterPoolWorker;
    }

    @Autowired
    public void setServerCacheDao(HokageServerCacheDao serverCacheDao) {
        this.serverCacheDao = serverCacheDao;
    }

    @Override
    public void onApplicationEvent(AvailabilityChangeEvent<ReadinessState> event) {
        log.info("listen readiness state: " + event.getState());

        if (!ObjectUtils.defaultIfNull(canBeMaster, false)) {
            log.info("unable to be master, return directly. if you want to be master, please set the property: 'system.report.info.handler=true'");
            return;
        }
        scheduledPoolWorker.getScheduledService().scheduleAtFixedRate(() -> {
            try {
                masterPoolWorker.getExecutorPool().execute(this::tryToBeMaster);
            } catch (Exception e) {
                log.error("try to be master error. errMsg: {}", e.getMessage());
            }
        }, 0, interval, TimeUnit.SECONDS);
    }

    private void tryToBeMaster() {

        int port = context.getWebServer().getPort();
        String localIp = acquireLocalIp();
        if (StringUtils.isBlank(localIp)) {
            return;
        }

        HokageServerReportInfoHandlerDO handlerDO = reportHandlerDao.selectById(Constant.MASTER_REPORT_ID);
        // 之前是master, 掉线后重启
        if (StringUtils.equals(localIp, handlerDO.getHandlerIp()) && port == handlerDO.getHandlerPort() && !masterPoolWorker.isMaster()) {
            log.info("ip: {}, port: {} be master.", localIp, port);
            masterPoolWorker.setMaster(true);
            // 缓存所有服务器client
            serverCacheDao.activeMetricClient();
            return;
        }

        boolean isHealth = IpAddressUtil.checkHealth(handlerDO.getHandlerIp(), handlerDO.getHandlerPort(), HEALTH_CHECK);
        if (isHealth) {
            return;
        }

        // 抢占master
        handlerDO.setHandlerIp(localIp)
                .setHandlerPort(port)
                .setStartTime(System.currentTimeMillis());

        Long result = reportHandlerDao.update(handlerDO);
        if (result > 0) {
            log.info("ip: {}, port: {} be master.", localIp, port);
            masterPoolWorker.setMaster(true);
            // 缓存所有服务器client
            serverCacheDao.activeMetricClient();
            return;
        }

        log.info("ip: {}, port: {} can't be master.", localIp, port);
        masterPoolWorker.setMaster(false);
        // 清空服务器client缓存
        serverCacheDao.invalidateMetricCache();
    }

    private String acquireLocalIp() {
        int port = context.getWebServer().getPort();
        List<String> ipList = IpAddressUtil.acquireIpList().stream()
                .filter(ip -> !StringUtils.equals(ip, Constant.LOCAL_HOST))
                .filter(ip -> IpAddressUtil.checkHealth(ip, port, HEALTH_CHECK))
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(ipList)) {
            log.warn("unable to get the client ip");
            return StringUtils.EMPTY;
        }
        log.info("client ip list: " + JSON.toJSONString(ipList));
        return ipList.get(0);
    }
}
