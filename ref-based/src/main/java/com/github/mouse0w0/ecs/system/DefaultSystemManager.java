package com.github.mouse0w0.ecs.system;

import com.github.mouse0w0.ecs.EntityManager;
import com.github.mouse0w0.ecs.EntityRef;
import com.github.mouse0w0.ecs.component.*;
import com.github.mouse0w0.ecs.util.BitArray;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class DefaultSystemManager implements SystemManager {

    private EntityManager entityManager;
    private ComponentManager componentManager;

    private List<RegisteredSystem> systems = new ArrayList<>();

    public DefaultSystemManager(EntityManager entityManager, ComponentManager componentManager) {
        this.entityManager = entityManager;
        this.componentManager = componentManager;
    }

    @Override
    public void register(Object object) {
        registerObject(object);
    }

    private void registerObject(Object object) {
        boolean foundSystem = false;
        for (Method method : object.getClass().getMethods()) {
            if (method.isAnnotationPresent(System.class)) {
                if (foundSystem)
                    throw new SystemRegistrationException("Cannot register more than one system in the object");
                registerSystem(object, method);
                foundSystem = true;
            }
        }
        if (!foundSystem) throw new SystemRegistrationException("Cannot find system");
    }

    private void registerSystem(Object owner, Method method) {
        if (Modifier.isStatic(method.getModifiers()))
            throw new SystemRegistrationException("Cannot register a static system for a object");

        Class<?>[] parameterTypes = method.getParameterTypes();

        if (parameterTypes.length == 0)
            throw new SystemRegistrationException("");

        if (!EntityRef.class.equals(parameterTypes[0]))
            throw new SystemRegistrationException("");

        List<ComponentType> componentTypes = new ArrayList<>();
        ComponentTypeFactory factory = componentManager.getComponentTypeFactory();

        for (int i = 1; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            if (Component.class.isAssignableFrom(parameterType)) {
                componentTypes.add(factory.get((Class<? extends Component>) parameterType));
            } else {
                throw new SystemRegistrationException("Unsupported system parameter type " + parameterType);
            }
        }

        registerSystem(owner, method, List.copyOf(componentTypes));
    }

    private void registerSystem(Object owner, Method method, List<ComponentType> componentTypes) {
        systems.add(new RegisteredSystem(owner, method, componentTypes, buildComponentTypeBits(componentTypes), buildComponentMappers(componentTypes)));
    }

    private BitArray buildComponentTypeBits(List<ComponentType> componentTypes) {
        BitArray bits = new BitArray();
        for (ComponentType type : componentTypes) {
            bits.mark(type.getId());
        }
        return bits;
    }

    private ComponentMapper[] buildComponentMappers(List<ComponentType> componentTypes) {
        List<ComponentMapper> mappers = new ArrayList<>();
        mappers.add(null);
        for (ComponentType type : componentTypes) {
            mappers.add(componentManager.getComponentMapper(type));
        }
        return mappers.toArray(ComponentMapper[]::new);
    }

    @Override
    public void update() {
        for (RegisteredSystem system : systems) {
            updateSystem(system);
        }
    }

    private void updateSystem(RegisteredSystem system) {
        BitArray systemComponentBits = system.getComponentBits();
        Object[] args = system.getArgumentArray();
        ComponentMapper[] componentMappers = system.getComponentMappers();
        for (EntityRef entity : entityManager.getEntities()) {
            int id = entity.getId();
            BitArray componentBits = componentManager.getComponentBits(id);
            if (!componentBits.contains(systemComponentBits)) continue;

            args[0] = entity;
            for (int i = 1, size = componentMappers.length; i < size; i++) {
                args[i] = componentMappers[i].get(id);
            }

            system.invoke();
        }
    }
}
