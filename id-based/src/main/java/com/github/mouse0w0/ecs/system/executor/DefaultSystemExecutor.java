package com.github.mouse0w0.ecs.system.executor;

import com.github.mouse0w0.ecs.EntityManager;
import com.github.mouse0w0.ecs.component.ComponentManager;
import com.github.mouse0w0.ecs.component.ComponentMapper;
import com.github.mouse0w0.ecs.system.SystemUpdateException;
import com.github.mouse0w0.ecs.system.invoker.SystemInvoker;
import com.github.mouse0w0.ecs.util.BitArray;
import com.github.mouse0w0.ecs.util.IntIterator;

import java.lang.reflect.Method;

public class DefaultSystemExecutor implements SystemExecutor {
    public static final SystemExecutorFactory FACTORY = new SystemExecutorFactory() {
        @Override
        public SystemExecutor create(EntityManager entityManager, Object owner, Method method, BitArray componentBits, ComponentMapper[] componentMappers) {
            SystemInvoker invoker = entityManager.getSystemManager().getInvokerFactory().create(owner, method, componentMappers);
            return new DefaultSystemExecutor(entityManager, owner, method, componentBits, invoker);
        }
    };

    private final EntityManager entityManager;
    private final ComponentManager componentManager;
    private final Object owner;
    private final Method method;
    private final BitArray componentBits;
    private final SystemInvoker invoker;

    private DefaultSystemExecutor(EntityManager entityManager, Object owner, Method method, BitArray componentBits, SystemInvoker invoker) {
        this.entityManager = entityManager;
        this.componentManager = entityManager.getComponentManager();
        this.owner = owner;
        this.method = method;
        this.componentBits = componentBits;
        this.invoker = invoker;
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
        IntIterator entities = entityManager.getEntities();
        while (entities.hasNext()) {
            int id = entities.next();
            BitArray componentBits = componentManager.getComponentBits(id);
            if (componentBits.contains(this.componentBits)) {
                try {
                    invoker.update(id);
                } catch (Exception e) {
                    throw new SystemUpdateException(e);
                }
            }
        }
    }
}
