package com.github.mouse0w0.ecs.system;

import com.github.mouse0w0.ecs.component.ComponentMapper;
import com.github.mouse0w0.ecs.util.BitArray;

import java.lang.reflect.Method;

public class RegisteredSystem {

    private Object owner;
    private Method method;
    private BitArray componentBits;
    private Object[] argumentArray;
    private ComponentMapper[] componentMappers;

    public RegisteredSystem(Object owner, Method method, BitArray componentBits, ComponentMapper[] componentMappers) {
        this.owner = owner;
        this.method = method;
        method.setAccessible(true);
        this.componentBits = componentBits;
        this.argumentArray = new Object[method.getParameterCount()];
        this.componentMappers = componentMappers;
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

    public Object[] getArgumentArray() {
        return argumentArray;
    }

    public ComponentMapper[] getComponentMappers() {
        return componentMappers;
    }

    public void invoke() {
        try {
            method.invoke(owner, argumentArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
