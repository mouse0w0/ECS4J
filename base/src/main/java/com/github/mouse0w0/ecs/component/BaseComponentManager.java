package com.github.mouse0w0.ecs.component;

import com.github.mouse0w0.ecs.util.BitArray;
import com.github.mouse0w0.ecs.util.ObjectArray;

public abstract class BaseComponentManager implements ComponentManager {

    private final ComponentTypeFactory componentTypeFactory = createComponentTypeFactory();

    private final ObjectArray<ComponentMapper> components = new ObjectArray<>(ComponentMapper.class);
    private final ObjectArray<BitArray> entityComponents = new ObjectArray<>(BitArray.class);

    protected abstract ComponentTypeFactory createComponentTypeFactory();

    @Override
    public ComponentTypeFactory getComponentTypeFactory() {
        return componentTypeFactory;
    }

    @Override
    public BitArray getComponentBits(int entityId) {
        return entityComponents.get(entityId);
    }

    @Override
    public ComponentMapper getComponentMapper(ComponentType type) {
        return components.get(type.getId());
    }

    @Override
    public <T extends Component> T getComponent(int entityId, ComponentType type) {
        return (T) components.get(type.getId()).get(entityId);
    }

    @Override
    public boolean hasComponent(int entityId, ComponentType type) {
        return getComponent(entityId, type) != null;
    }

    @Override
    public <T extends Component> T addComponent(int entityId, T component) {
        components.get(componentTypeFactory.get(component.getClass()).getId()).set(entityId, component);
        return component;
    }

    @Override
    public <T extends Component> T addComponent(int entityId, ComponentType type, T component) {
        components.get(type.getId()).set(entityId, component);
        return component;
    }

    @Override
    public void removeComponent(int entityId, ComponentType type) {
        components.get(type.getId()).set(entityId, null);
    }

}
