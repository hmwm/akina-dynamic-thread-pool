package com.akina.middleware.dynamic.thread.pool.sdk.trigger.job;

import com.akina.middleware.dynamic.thread.pool.common.entity.ThreadPoolConfigEntity;
import com.akina.middleware.dynamic.thread.pool.sdk.config.DynamicThreadPoolAutoConfig;
import com.akina.middleware.dynamic.thread.pool.sdk.domain.DynamicThreadPoolService;
import com.akina.middleware.dynamic.thread.pool.sdk.domain.IDynamicThreadPoolService;

import com.akina.middleware.dynamic.thread.pool.sdk.registry.IRegistry;
import com.alibaba.fastjson2.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

public class ThreadPoolDataReportJob {

    private final Logger logger = LoggerFactory.getLogger(DynamicThreadPoolAutoConfig.class);

    private final IDynamicThreadPoolService dynamicThreadPoolService;

    private final IRegistry registry;


    public ThreadPoolDataReportJob(DynamicThreadPoolService dynamicThreadPoolService, IRegistry registry) {
        this.dynamicThreadPoolService = dynamicThreadPoolService;
        this.registry = registry;
    }

    @Scheduled(cron = "0/20 * * * * ?")
    public void exeReportThreadPoolList(){
        List<ThreadPoolConfigEntity> threadPoolConfigEntities = dynamicThreadPoolService.queryThreadPoolList();
        registry.reportThreadPool(threadPoolConfigEntities);
        logger.info("动态线程池，上报线程池信息：{}", JSON.toJSONString(threadPoolConfigEntities));

        for (ThreadPoolConfigEntity threadPoolConfigEntity : threadPoolConfigEntities){
            registry.reportThreadPoolConfigParameter(threadPoolConfigEntity);
            logger.info("动态线程池，上报线程池配置：{}", JSON.toJSONString(threadPoolConfigEntity));
        }
    }
}
