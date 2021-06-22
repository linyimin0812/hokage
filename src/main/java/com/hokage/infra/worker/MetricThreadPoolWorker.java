package com.hokage.infra.worker;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author yiminlin
 * @date 2021/06/22 11:29 pm
 * @description metric thread pool worker
 **/
@Data
@Slf4j
@Component
public class MetricThreadPoolWorker {
    private ThreadPoolExecutor executorPool = null;

    @PostConstruct
    public void init() {
        log.info("Initializing MetricThreadPoolWorker [\"metric-thread-worker-pool\"]");
        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("metric-thread-worker-pool-%d").build();
        int coreNum = Runtime.getRuntime().availableProcessors();
        executorPool = new ThreadPoolExecutor(
                coreNum,
                coreNum,
                60,
                TimeUnit.MINUTES,
                new LinkedBlockingQueue<Runnable>(coreNum),
                factory,
                new ThreadPoolExecutor.AbortPolicy()
        );
    }
}
