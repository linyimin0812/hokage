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
 * @date 2021/06/29 12:28 am
 * @description bat command thread pool
 **/
@Data
@Slf4j
@Component
public class BatCommandThreadPoolWorker {
    private ThreadPoolExecutor executorPool = null;

    @PostConstruct
    public void init() {
        log.info("Initializing BatCommandThreadPoolWorker [\"bat-command-worker-thread-pool\"]");
        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("bat-command-worker-thread-pool-%d").build();
        int coreNum = Runtime.getRuntime().availableProcessors();
        executorPool = new ThreadPoolExecutor(
                coreNum,
                coreNum,
                60,
                TimeUnit.MINUTES,
                new LinkedBlockingQueue<Runnable>(1024),
                factory,
                new ThreadPoolExecutor.AbortPolicy()
        );
    }
}
