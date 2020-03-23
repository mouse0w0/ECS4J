package com.github.mouse0w0.ecs.system;

import com.github.mouse0w0.ecs.EntityManager;
import com.github.mouse0w0.ecs.component.ComponentManager;
import com.github.mouse0w0.ecs.system.invoker.SystemInvoker;
import com.github.mouse0w0.ecs.util.BitArray;
import com.github.mouse0w0.ecs.util.IntIterator;

import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

public class MTRegisteredSystem {

    private static final int THREAD_COUNT = 12;

    private Object owner;
    private Method method;
    private BitArray componentBits;
    private SystemInvoker invoker;

    public MTRegisteredSystem(Object owner, Method method, BitArray componentBits, SystemInvoker invoker) {
        this.owner = owner;
        this.method = method;
        this.componentBits = componentBits;
        this.invoker = invoker;
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

    public void update(EntityManager entityManager, ComponentManager componentManager) {
        int capacity = entityManager.capacity();
        int eachThreadEntityCount = capacity / THREAD_COUNT;
        CompletableFuture[] futures = new CompletableFuture[THREAD_COUNT];
        for (int i = 0, size = THREAD_COUNT - 1; i < size; i++) {
            futures[i] = CompletableFuture.runAsync(new SystemUpdateTask(entityManager, componentManager,
                    i * eachThreadEntityCount, eachThreadEntityCount));
        }
        int lastThreadOffset = (THREAD_COUNT - 1) * eachThreadEntityCount;
        futures[THREAD_COUNT - 1] = CompletableFuture.runAsync(new SystemUpdateTask(entityManager, componentManager,
                lastThreadOffset, capacity - lastThreadOffset));
        CompletableFuture.allOf(futures).join();
    }

    private class SystemUpdateTask implements Runnable {

        private final EntityManager entityManager;
        private final ComponentManager componentManager;
        private final int first;
        private final int count;

        public SystemUpdateTask(EntityManager entityManager, ComponentManager componentManager, int first, int count) {
            this.entityManager = entityManager;
            this.componentManager = componentManager;
            this.first = first;
            this.count = count;
        }

        @Override
        public void run() {
            IntIterator entities = entityManager.getEntities(first, count);
            while (entities.hasNext()) {
                int id = entities.next();
                BitArray componentBits = componentManager.getComponentBits(id);
                if (componentBits.contains(MTRegisteredSystem.this.componentBits)) {
                    try {
                        invoker.update(id);
                    } catch (Exception e) {
                        throw new SystemUpdateException(e);
                    }
                }
            }
        }
    }
}
