package com.hokage.infra.worker;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author yiminlin
 * @date 2021/06/11 1:44 am
 * @description work thread pool
 **/
@Data
@Component
public class ThreadPoolWorker {
    private ThreadPoolExecutor executorPool = null;

    @PostConstruct
    public void init() {
        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("worker-thread-pool-%d").build();
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
