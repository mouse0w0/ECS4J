package com.github.mouse0w0.ecs.system;

import com.github.mouse0w0.ecs.EntityManager;
import com.github.mouse0w0.ecs.system.executor.DefaultSystemExecutor;
import com.github.mouse0w0.ecs.system.executor.SystemExecutorFactory;
import com.github.mouse0w0.ecs.system.invoker.ReflectionSystemInvoker;
import com.github.mouse0w0.ecs.system.invoker.SystemInvokerFactory;

import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public class DefaultSystemManager extends BaseSystemManager {

    public DefaultSystemManager(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected SystemExecutorFactory createExecutorFactory() {
        return DefaultSystemExecutor.FACTORY;
    }

    @Override
    protected SystemInvokerFactory createInvokerFactory() {
        return ReflectionSystemInvoker.FACTORY;
    }
}
