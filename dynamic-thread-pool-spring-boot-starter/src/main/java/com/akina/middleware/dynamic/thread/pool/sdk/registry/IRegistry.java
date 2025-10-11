package com.akina.middleware.dynamic.thread.pool.sdk.registry;



import com.akina.middleware.dynamic.thread.pool.common.entity.ThreadPoolConfigEntity;

import java.util.List;

public interface IRegistry {

    public void reportThreadPool(List<ThreadPoolConfigEntity> threadPoolConfigEntities);

    public void reportThreadPoolConfigParameter(ThreadPoolConfigEntity threadPoolConfigEntity);
}
