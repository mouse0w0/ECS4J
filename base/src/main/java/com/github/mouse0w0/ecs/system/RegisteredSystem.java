package com.github.mouse0w0.ecs.system;

import com.github.mouse0w0.ecs.component.ComponentMapper;
import com.github.mouse0w0.ecs.component.ComponentType;
import com.github.mouse0w0.ecs.util.BitArray;

import java.lang.reflect.Method;
import java.util.List;

public class RegisteredSystem {

    private Object owner;
    private Method method;
    private List<ComponentType> componentTypes;
    private BitArray componentBits;
    private Object[] argumentArray;
    private ComponentMapper[] componentMappers;

    public RegisteredSystem(Object owner, Method method, List<ComponentType> componentTypes, BitArray componentBits, ComponentMapper[] componentMappers) {
        this.owner = owner;
        this.method = method;
        method.setAccessible(true);
        this.componentTypes = componentTypes;
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

    public List<ComponentType> getComponentTypes() {
        return componentTypes;
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
