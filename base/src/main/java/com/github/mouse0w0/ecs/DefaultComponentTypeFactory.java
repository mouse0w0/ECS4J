package com.github.mouse0w0.ecs;

import com.github.mouse0w0.ecs.component.Component;
import com.github.mouse0w0.ecs.component.ComponentType;
import com.github.mouse0w0.ecs.component.ComponentTypeFactory;
import com.github.mouse0w0.ecs.util.ObjectArray;

import java.util.IdentityHashMap;
import java.util.Map;

public class DefaultComponentTypeFactory implements ComponentTypeFactory {

    private final ObjectArray<ComponentType> types = new ObjectArray<>();
    private final Map<Class<? extends Component>, ComponentType> classToType = new IdentityHashMap<>();

    @Override
    public ComponentType get(Class<? extends Component> type) {
        ComponentType componentType = classToType.get(type);
        if (componentType == null) {
            componentType = new ComponentType(classToType.size(), type);
            types.set(componentType.getId(), componentType);
            classToType.put(type, componentType);
        }
        return componentType;
    }

    @Override
    public ComponentType get(int componentId) {
        return types.get(componentId);
    }
}
