package com.github.mouse0w0.ecs.system;

import com.github.mouse0w0.ecs.EntityManager;
import com.github.mouse0w0.ecs.component.ComponentManager;
import com.github.mouse0w0.ecs.component.ComponentMapper;
import com.github.mouse0w0.ecs.system.executor.SystemExecutor;
import com.github.mouse0w0.ecs.system.executor.SystemExecutorFactory;
import com.github.mouse0w0.ecs.system.invoker.SystemInvoker;
import com.github.mouse0w0.ecs.system.invoker.SystemInvokerFactory;
import com.github.mouse0w0.ecs.util.BitArray;
import com.github.mouse0w0.ecs.util.IntIterator;

import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

public class MultiThreadSystemExecutor implements SystemExecutor {

    public static final SystemExecutorFactory FACTORY = new SystemExecutorFactory() {
        @Override
        public SystemExecutor create(EntityManager entityManager, Object owner, Method method, BitArray componentBits, ComponentMapper[] componentMappers) {
            return new MultiThreadSystemExecutor(entityManager, owner, method, componentBits, componentMappers);
        }
    };

    private static final int THREAD_COUNT = 12;

    private final EntityManager entityManager;
    private final ComponentManager componentManager;
    private final Object owner;
    private final Method method;
    private final BitArray componentBits;
    private final ComponentMapper[] componentMappers;

    private final Task[] tasks = new Task[THREAD_COUNT];

    private MultiThreadSystemExecutor(EntityManager entityManager, Object owner, Method method, BitArray componentBits, ComponentMapper[] componentMappers) {
        this.entityManager = entityManager;
        this.componentManager = entityManager.getComponentManager();
        this.owner = owner;
        this.method = method;
        this.componentBits = componentBits;
        this.componentMappers = componentMappers;

        SystemInvokerFactory invokerFactory = entityManager.getSystemManager().getInvokerFactory();
        for (int i = 0; i < tasks.length; i++) {
            tasks[i] = new Task(invokerFactory.create(owner, method, componentMappers));
        }
    }

    @Override
    public Object getOwner() {
        return owner;
    }

    @Override
    public Method getMethod() {
        return method;
    }

    @Override
    public void execute() {
        int capacity = entityManager.capacity();
        int eachThreadEntityCount = capacity / THREAD_COUNT;
        var futures = new CompletableFuture[THREAD_COUNT];
        for (int i = 0, size = THREAD_COUNT - 1; i < size; i++) {
            Task task = tasks[i];
            task.setup(i * eachThreadEntityCount, eachThreadEntityCount);
            futures[i] = CompletableFuture.runAsync(task);
        }
        int lastThreadOffset = (THREAD_COUNT - 1) * eachThreadEntityCount;
        Task lastTask = tasks[THREAD_COUNT - 1];
        lastTask.setup(lastThreadOffset, capacity - lastThreadOffset);
        futures[THREAD_COUNT - 1] = CompletableFuture.runAsync(lastTask);
        CompletableFuture.allOf(futures).join();
    }

    private class Task implements Runnable {

        private final SystemInvoker invoker;

        private int first;
        private int count;

        public Task(SystemInvoker invoker) {
            this.invoker = invoker;
        }

        public void setup(int first, int count) {
            this.first = first;
            this.count = count;
        }

        @Override
        public void run() {
            IntIterator entities = entityManager.getEntities(first, count);
            while (entities.hasNext()) {
                int id = entities.next();
                BitArray componentBits = componentManager.getComponentBits(id);
                if (componentBits.contains(MultiThreadSystemExecutor.this.componentBits)) {
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
