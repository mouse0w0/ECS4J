package com.github.mouse0w0.ecs.system;

import com.github.mouse0w0.ecs.EntityManager;
import com.github.mouse0w0.ecs.component.ComponentManager;
import com.github.mouse0w0.ecs.system.invoker.SystemInvoker;
import com.github.mouse0w0.ecs.util.BitArray;
import com.github.mouse0w0.ecs.util.IntIterator;

import javax.annotation.concurrent.NotThreadSafe;
import java.lang.reflect.Method;

@NotThreadSafe
public class RegisteredSystem {

    private Object owner;
    private Method method;
    private BitArray componentBits;
    private SystemInvoker invoker;

    public RegisteredSystem(Object owner, Method method, BitArray componentBits, SystemInvoker invoker) {
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
        IntIterator entities = entityManager.getEntities();
        while (entities.hasNext()) {
            int id = entities.next();
            BitArray componentBits = componentManager.getComponentBits(id);
            if (componentBits.contains(componentBits)) {
                try {
                    invoker.update(id);
                } catch (Exception e) {
                    throw new SystemUpdateException(e);
                }
            }
        }
    }
}
