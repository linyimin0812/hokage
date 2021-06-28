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
 * @date 2021/06/29 12:27 am
 * @description bat command scheduled pool
 **/
@Data
@Slf4j
@Component
public class BatCommandScheduledPoolWorker {
    private ScheduledExecutorService scheduledService = null;

    @PostConstruct
    public void init() {
        log.info("Initializing BatCommandScheduledPoolWorker [\"bat-command-scheduled-service-worker-pool\"]");
        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("bat-command-scheduled-service-worker-pool-%d").build();
        scheduledService = new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), factory);
    }
}
