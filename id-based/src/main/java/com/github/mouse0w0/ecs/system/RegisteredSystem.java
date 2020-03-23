package com.github.mouse0w0.ecs.system;

import com.github.mouse0w0.ecs.EntityManager;
import com.github.mouse0w0.ecs.component.ComponentManager;
import com.github.mouse0w0.ecs.component.ComponentMapper;
import com.github.mouse0w0.ecs.util.BitArray;
import com.github.mouse0w0.ecs.util.IntIterator;

import javax.annotation.concurrent.NotThreadSafe;
import java.lang.reflect.Method;

@NotThreadSafe
public class RegisteredSystem {

    private Object owner;
    private Method method;
    private BitArray componentBits;
    private ComponentMapper[] componentMappers;
    private int componentCount;

    private Object[] args;

    public RegisteredSystem(Object owner, Method method, BitArray componentBits, ComponentMapper[] componentMappers) {
        this.owner = owner;
        this.method = method;
        method.setAccessible(true);
        this.componentBits = componentBits;
        this.args = new Object[method.getParameterCount()];
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
        IntIterator entities = entityManager.getEntities();
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
