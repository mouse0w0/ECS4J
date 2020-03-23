package com.github.mouse0w0.ecs.system.executor;

import com.github.mouse0w0.ecs.EntityManager;
import com.github.mouse0w0.ecs.component.ComponentMapper;
import com.github.mouse0w0.ecs.util.BitArray;

import java.lang.reflect.Method;

public interface SystemExecutorFactory {
    SystemExecutor create(EntityManager entityManager, Object owner, Method method, BitArray componentBits, ComponentMapper[] componentMappers);
}
