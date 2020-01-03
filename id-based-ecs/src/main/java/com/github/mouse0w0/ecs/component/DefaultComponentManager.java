package com.github.mouse0w0.ecs.component;

import com.github.mouse0w0.ecs.util.BitArray;
import com.github.mouse0w0.ecs.util.ObjectArray;

public final class DefaultComponentManager implements ComponentManager {

    private final ComponentTypeFactory typeFactory = new ComponentTypeFactory();

    private final ObjectArray<ComponentMapper> components = new ObjectArray<>(ComponentMapper.class);
    private final ObjectArray<BitArray> entityComponents = new ObjectArray<>(BitArray.class);

    @Override
    public int register(Class<? extends Component> clazz) {
        int id = typeFactory.register(clazz);
        components.set(id, new ComponentMapper(id, clazz));
        return id;
    }

    @Override
    public int getComponentId(Class<? extends Component> clazz) {
        return typeFactory.get(clazz);
    }

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
