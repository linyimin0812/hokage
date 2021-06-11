package com.hokage.infra.worker;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

/**
 * @author yiminlin
 * @date 2021/05/29 12:09 pm
 * @description schedule thread pool
 **/
@Data
@Slf4j
@Component
public class ScheduledThreadPoolWorker {
    private ScheduledExecutorService scheduledService = null;

    @PostConstruct
    public void init() {
        log.info("Initializing ScheduledThreadPoolWorker [\"scheduled-service-worker-pool\"]");
        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("scheduled-service-worker-pool-%d").build();
        scheduledService = new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), factory);
    }
}
