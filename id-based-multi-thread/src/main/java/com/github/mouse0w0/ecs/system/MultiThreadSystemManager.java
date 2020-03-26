package com.github.mouse0w0.ecs.system;

import com.github.mouse0w0.ecs.EntityManager;
import com.github.mouse0w0.ecs.system.executor.SystemExecutorFactory;

public class MultiThreadSystemManager extends DefaultSystemManager {
    public MultiThreadSystemManager(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected SystemExecutorFactory createExecutorFactory() {
        return MultiThreadSystemExecutor.FACTORY;
    }
}
