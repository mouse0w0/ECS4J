package com.github.mouse0w0.ecs.component;

import com.github.mouse0w0.ecs.util.BitArray;
import com.github.mouse0w0.ecs.util.ObjectArray;

public abstract class BaseComponentManager implements ComponentManager {

    private final ComponentTypeFactory componentTypeFactory = createComponentTypeFactory();

    private final ObjectArray<ComponentMapper> components = new ObjectArray<>(ComponentMapper.class);
    private final ObjectArray<BitArray> entityComponents = new ObjectArray<>(BitArray.class);

    protected abstract ComponentTypeFactory createComponentTypeFactory();

    @Override
    public BitArray getComponentBits(int entityId) {
        return entityComponents.get(entityId);
    }

    @Override
    public ComponentMapper getComponentMapper(int componentId) {
        return components.get(componentId);
    }

    @Override
    public <T extends Component> T getComponent(int entityId, int componentId) {
        return (T) components.get(componentId).get(entityId);
    }

    @Override
    public boolean hasComponent(int entityId, int componentId) {
        return entityComponents.get(entityId).get(componentId);
    }

    @Override
    public <T extends Component> T addComponent(int entityId, int componentId, T component) {
        components.get(componentId).set(entityId, component);
        entityComponents.get(entityId).mark(componentId);
        return component;
    }

    @Override
    public void removeComponent(int entityId, int componentId) {
        components.get(componentId).set(entityId, null);
        entityComponents.get(entityId).clear(componentId);
    }

    @Override
    public void ensureCapacity(int minCapability) {
        for (int i = 0, size = components.size(); i < size; i++) {
            components.get(i).ensureCapacity(minCapability);
        }
        entityComponents.ensureCapacity(minCapability);
    }
}
