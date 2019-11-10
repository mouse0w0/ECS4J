package com.github.mouse0w0.ecs.impl;

import com.github.mouse0w0.ecs.component.Component;
import com.github.mouse0w0.ecs.component.ComponentManager;
import com.github.mouse0w0.ecs.component.ComponentType;
import com.github.mouse0w0.ecs.component.ComponentTypeFactory;
import com.github.mouse0w0.ecs.util.ObjectArray;

public abstract class BaseComponentManager implements ComponentManager {

    private final ComponentTypeFactory componentTypeFactory = createComponentTypeFactory();

    private final ObjectArray<ComponentMapper> components = new ObjectArray<>();

    protected abstract ComponentTypeFactory createComponentTypeFactory();

    @Override
    public <T extends Component> T getComponent(int entityId, ComponentType type) {
        return null;
    }

    @Override
    public boolean hasComponent(int entityId, ComponentType type) {
        return false;
    }

    @Override
    public <T extends Component> T addComponent(int entityId, T component) {
        return null;
    }

    @Override
    public void removeComponent(int entityId, ComponentType type) {

    }

    private class ComponentMapper {
        private final ComponentType type;
        private final ObjectArray<Component> components = new ObjectArray<>();

        public ComponentMapper(ComponentType type) {
            this.type = type;
        }
    }
}
