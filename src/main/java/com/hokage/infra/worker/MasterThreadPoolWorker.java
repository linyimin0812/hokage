package com.hokage.infra.worker;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
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
public class MasterThreadPoolWorker {
    private ThreadPoolExecutor executorPool = null;

    @Value("${system.report.info.handler}")
    private boolean canBeMaster;
    private boolean isMaster;
    @PostConstruct
    public void init() {
        if (!ObjectUtils.defaultIfNull(canBeMaster, false)) {
            return;
        }
        log.info("Initializing MasterThreadPoolWorker [\"master-thread-worker-pool\"]");
        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("master-thread-worker-pool-%d").build();
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
