package com.github.mouse0w0.ecs.system;

import com.github.mouse0w0.ecs.EntityManager;
import com.github.mouse0w0.ecs.component.Component;
import com.github.mouse0w0.ecs.component.ComponentManager;
import com.github.mouse0w0.ecs.component.ComponentMapper;
import com.github.mouse0w0.ecs.util.BitArray;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class DefaultSystemManager implements SystemManager {

    private EntityManager entityManager;
    private ComponentManager componentManager;

    private List<RegisteredSystem> systems = new ArrayList<>();

    public DefaultSystemManager(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.componentManager = entityManager.getComponentManager();
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

        if (!int.class.equals(parameterTypes[0]))
            throw new SystemRegistrationException("");

        BitArray componentBits = new BitArray();
        ComponentMapper[] componentMappers = new ComponentMapper[parameterTypes.length - 1];

        for (int i = 1, size = parameterTypes.length; i < size; i++) {
            Class<?> parameterType = parameterTypes[i];
            if (Component.class.isAssignableFrom(parameterType)) {
                int componentId = componentManager.getComponentId((Class<? extends Component>) parameterType);
                componentBits.mark(componentId);
                componentMappers[i - 1] = componentManager.getComponentMapper(componentId);
            } else {
                throw new SystemRegistrationException("Unsupported system parameter type " + parameterType);
            }
        }

        systems.add(new RegisteredSystem(owner, method, componentBits, componentMappers));
    }

    @Override
    public void update() {
        for (RegisteredSystem system : systems) {
            system.update(entityManager, componentManager);
        }
    }
}
