package com.github.mouse0w0.ecs.system;

import com.github.mouse0w0.ecs.EntityManager;
import com.github.mouse0w0.ecs.component.ComponentManager;
import com.github.mouse0w0.ecs.component.ComponentMapper;
import com.github.mouse0w0.ecs.util.BitArray;
import com.github.mouse0w0.ecs.util.IntIterator;

import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisteredSystem {

    private static final int THREAD_COUNT = 12;
    private static final ExecutorService EXECUTOR;

    static {
        EXECUTOR = Executors.newFixedThreadPool(THREAD_COUNT);
    }

    private Object owner;
    private Method method;
    private BitArray componentBits;
    private ComponentMapper[] componentMappers;
    private int componentCount;

    public RegisteredSystem(Object owner, Method method, BitArray componentBits, ComponentMapper[] componentMappers) {
        this.owner = owner;
        this.method = method;
        method.setAccessible(true);
        this.componentBits = componentBits;
        this.componentMappers = componentMappers;
        this.componentCount = componentMappers.length;
    }

    public Object getOwner() {
        return owner;
    }

    public Method getMethod() {
        return method;
    }

    public BitArray getComponentBits() {
        return componentBits;
    }

    public ComponentMapper[] getComponentMappers() {
        return componentMappers;
    }

    public void update(EntityManager entityManager, ComponentManager componentManager) {
        int capacity = entityManager.capacity();
        int eachThreadEntityCount = capacity / THREAD_COUNT;
        int parameterCount = method.getParameterCount();
        CompletableFuture[] futures = new CompletableFuture[THREAD_COUNT];
        for (int i = 0, size = THREAD_COUNT - 1; i < size; i++) {
            futures[i] = CompletableFuture.runAsync(new SystemUpdateTask(entityManager, componentManager,
                    i * eachThreadEntityCount, eachThreadEntityCount, parameterCount));
        }
        int lastThreadOffset = (THREAD_COUNT - 1) * eachThreadEntityCount;
        futures[THREAD_COUNT - 1] = CompletableFuture.runAsync(new SystemUpdateTask(entityManager, componentManager,
                lastThreadOffset, capacity - lastThreadOffset, parameterCount));
        CompletableFuture.allOf(futures).join();
    }

    private class SystemUpdateTask implements Runnable {

        private final EntityManager entityManager;
        private final ComponentManager componentManager;
        private final int first;
        private final int count;
        private final Object[] args;

        public SystemUpdateTask(EntityManager entityManager, ComponentManager componentManager, int first, int count, int parameterCount) {
            this.entityManager = entityManager;
            this.componentManager = componentManager;
            this.first = first;
            this.count = count;
            this.args = new Object[parameterCount];
        }

        @Override
        public void run() {
            IntIterator entities = entityManager.getEntities(first, count);
            while (entities.hasNext()) {
                int id = entities.next();
                BitArray componentBits = componentManager.getComponentBits(id);
                if (!componentBits.contains(componentBits)) continue;

                args[0] = id;
                for (int i = 1; i < componentCount; i++) {
                    args[i] = componentMappers[i].get(id);
                }

                try {
                    method.invoke(owner, args);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
