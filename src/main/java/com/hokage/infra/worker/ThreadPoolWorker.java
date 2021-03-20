package com.hokage.infra.worker;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
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
@Component
public class ThreadWorkerPool {
    private ThreadPoolExecutor executorPool = null;

    @PostConstruct
    public void init() {
        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("ssh-worker-pool").build();
        executorPool = new ThreadPoolExecutor(
                8,
                32,
                60,
                TimeUnit.MINUTES,
                new LinkedBlockingQueue<Runnable>(1024),
                factory,
                new ThreadPoolExecutor.AbortPolicy()
        );
    }
}
