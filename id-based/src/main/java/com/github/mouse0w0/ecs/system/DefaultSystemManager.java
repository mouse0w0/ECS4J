package com.github.mouse0w0.ecs.system;

import com.github.mouse0w0.ecs.EntityManager;
import com.github.mouse0w0.ecs.system.invoker.ReflectionSystemInvoker;
import com.github.mouse0w0.ecs.system.invoker.SystemInvokerFactory;

import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public class DefaultSystemManager extends BaseSystemManager {

    public DefaultSystemManager(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected SystemInvokerFactory createSystemInvokerFactory() {
        return ReflectionSystemInvoker.FACTORY;
    }
}
