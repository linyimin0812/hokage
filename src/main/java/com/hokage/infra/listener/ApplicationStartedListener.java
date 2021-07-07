package com.hokage.infra.listener;

import com.alibaba.fastjson.JSON;
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
    private static final String LOCAL_HOST = "127.0.0.1";

    @Value("${system.report.info.handler}")
    private boolean canBeMaster;

    private ServletWebServerApplicationContext context;

    @Autowired
    public void setContext(ServletWebServerApplicationContext context) {
        this.context = context;
    }

    @Override
    public void onApplicationEvent(AvailabilityChangeEvent<ReadinessState> event) {
        log.info("listen readiness state: " + event.getState());

        if (ObjectUtils.defaultIfNull(canBeMaster, false)) {
            log.info("unable to be master, return directly. if you want to be master, please set the property: 'system.report.info.handler=true'");
            return;
        }

        int port = context.getWebServer().getPort();
        List<String> ipList = IpAddressUtil.acquireIpList().stream()
                .filter(ip -> !StringUtils.equals(ip, LOCAL_HOST))
                .filter(ip -> IpAddressUtil.checkHealth(ip, port, HEALTH_CHECK))
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(ipList)) {
            log.warn("unable to get the client ip");
            return;
        }
        log.info("client ip list: " + JSON.toJSONString(ipList));

        // TODO: 抢占成为master
    }
}
