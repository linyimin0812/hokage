package com.hokage.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;


/**
 * @author yiminlin
 * @date 2021/05/29 8:12 am
 * @description base cache dao
 **/
@Slf4j
public class BaseCacheDao {
    public <K, V> Cache<K, V> buildDefaultLocalCache() {
        return CacheBuilder.newBuilder()
                .initialCapacity(1024)
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .removalListener(notification -> {
                    log.info("key: {}, value: {} is removed for {}", notification.getKey(), notification.getValue(), notification.getCause());
                })
                .build();
    }
}
