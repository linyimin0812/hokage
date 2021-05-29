package com.hokage.infra.worker;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

/**
 * @author yiminlin
 * @date 2021/05/29 12:09 pm
 * @description schedule thread pool
 **/
@Data
@Component
public class ScheduledThreadPoolWorker {
    private ScheduledExecutorService scheduledService = null;

    @PostConstruct
    public void init() {
        ThreadFactory factory = new ThreadFactoryBuilder().setNameFormat("scheduled-service-worker-pool").build();
        scheduledService = new ScheduledThreadPoolExecutor(1, factory);
    }
}
