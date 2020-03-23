package com.github.mouse0w0.ecs.system.invoker;

import com.github.mouse0w0.ecs.component.ComponentMapper;

import java.lang.reflect.Method;

public class ReflectionSystemInvoker implements SystemInvoker {

    public static final SystemInvokerFactory FACTORY = new SystemInvokerFactory() {
        @Override
        public SystemInvoker create(Object owner, Method method, ComponentMapper[] mappers) {
            return new ReflectionSystemInvoker(owner, method, mappers);
        }
    };

    private final Object owner;
    private final Method method;
    private final ComponentMapper[] mappers;

    private final Object[] args;

    public ReflectionSystemInvoker(Object owner, Method method, ComponentMapper[] mappers) {
        this.owner = owner;
        this.method = method;
        this.method.setAccessible(true);
        this.mappers = mappers;
        this.args = new Object[method.getParameterCount()];
    }

    @Override
    public void update(int entityId) throws Exception {
        args[0] = entityId;
        for (int i = 1, size = args.length; i < size; i++) {
            args[i] = mappers[i].get(entityId);
        }
        method.invoke(owner, args);
    }
}
