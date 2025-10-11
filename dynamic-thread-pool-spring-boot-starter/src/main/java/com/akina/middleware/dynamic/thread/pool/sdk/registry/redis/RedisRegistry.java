package com.akina.middleware.dynamic.thread.pool.sdk.registry.redis;


import com.akina.middleware.dynamic.thread.pool.common.entity.ThreadPoolConfigEntity;
import com.akina.middleware.dynamic.thread.pool.common.enums.RegistryEnumVO;
import com.akina.middleware.dynamic.thread.pool.sdk.registry.IRegistry;
import org.redisson.api.RBucket;
import org.redisson.api.RList;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.Duration;
import java.util.List;

public class RedisRegistry implements IRegistry {

    private final Logger logger = LoggerFactory.getLogger(RedisRegistry.class);

    private final RedissonClient redissonClient;

    public RedisRegistry(@Qualifier RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public void reportThreadPool(List<ThreadPoolConfigEntity> threadPoolConfigEntities) {
        // 旧版本
//        RList<ThreadPoolConfigEntity> list = redissonClient.getList(RegistryEnumVO.THREAD_POOL_COFIG_LIST_KEY.getKey());
//        list.delete();
//        list.addAll(threadPoolConfigEntities);

        // 新版去重兼容
        RSet<ThreadPoolConfigEntity> set = redissonClient.getSet(RegistryEnumVO.THREAD_POOL_CONFIG_SET_KEY.getKey());
        set.addAll(threadPoolConfigEntities);
        logger.info("动态线程池上报成功，共 {} 个配置项，已自动去重。", threadPoolConfigEntities.size());

        if (redissonClient.getKeys().countExists(RegistryEnumVO.THREAD_POOL_CONFIG_LIST_KEY.getKey()) == 0) {
            RList<ThreadPoolConfigEntity> list = redissonClient.getList(RegistryEnumVO.THREAD_POOL_CONFIG_LIST_KEY.getKey());
            list.addAll(threadPoolConfigEntities);
        }

        logger.info("动态线程池配置上报成功：写入 RSet（新结构），兼容 RList（旧结构）");
    }

    @Override
    public void reportThreadPoolConfigParameter(ThreadPoolConfigEntity threadPoolConfigEntity) {
        String cacheKey = RegistryEnumVO.THREAD_POOL_CONFIG_PARAMETER_LIST_KEY + "_" + threadPoolConfigEntity.getAppName() + "_" + threadPoolConfigEntity.getThreadPoolName();
        RBucket<ThreadPoolConfigEntity> bucket = redissonClient.getBucket(cacheKey);
        bucket.set(threadPoolConfigEntity, Duration.ofDays(30));
    }
}
