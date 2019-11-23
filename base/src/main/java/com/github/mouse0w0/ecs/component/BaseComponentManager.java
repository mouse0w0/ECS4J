package com.github.mouse0w0.ecs.component;

import com.github.mouse0w0.ecs.util.ObjectArray;

public abstract class BaseComponentManager implements ComponentManager {

    private final ComponentTypeFactory componentTypeFactory = createComponentTypeFactory();

    private final ObjectArray<ComponentMapper> components = new ObjectArray<>();

    protected abstract ComponentTypeFactory createComponentTypeFactory();

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
    public void removeComponent(int entityId, ComponentType type) {
        components.get(type.getId()).set(entityId, null);
    }

    private static class ComponentMapper {
        private final ComponentType type;
        private final ObjectArray components;

        public ComponentMapper(ComponentType type) {
            this.type = type;
            this.components = new ObjectArray(type.getType());
        }

        public ComponentType getType() {
            return type;
        }

        public Object get(int index) {
            return components.get(index);
        }

        public void set(int index, Component value) {
            components.unsafeSet(index, value);
        }

        public void ensureCapacity(int minCapacity) {
            components.ensureCapacity(minCapacity);
        }
    }
}
