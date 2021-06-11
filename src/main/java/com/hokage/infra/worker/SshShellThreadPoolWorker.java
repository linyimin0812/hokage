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
 * thread worker pool
 * @author yiminlin
 */
@Data
@Slf4j
@Component
public class SshShellThreadPoolWorker {
    private ThreadPoolExecutor executorPool = null;

    @PostConstruct
    public void init() {
        log.info("Initializing SshShellThreadPoolWorker [\"shell-worker-pool\"]");
        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("shell-worker-pool-%d").build();
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
