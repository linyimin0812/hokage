package com.hokage.infra.worker;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

/**
 * @author yiminlin
 * @date 2021/06/22 11:28 pm
 * @description metric schedule thread pool worker
 **/
@Data
@Slf4j
@Component
public class MetricScheduledThreadPoolWorker {
    private ScheduledExecutorService scheduledService = null;

    @PostConstruct
    public void init() {
        log.info("Initializing MetricScheduledThreadPoolWorker [\"metric-scheduled-service-worker-pool\"]");
        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("metric-scheduled-service-worker-pool-%d").build();
        scheduledService = new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), factory);
    }
}
