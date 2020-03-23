package com.github.mouse0w0.ecs.component;

import com.github.mouse0w0.ecs.util.BitArray;

public interface ComponentManager {
    ComponentTypeFactory getComponentTypeFactory();

    BitArray getComponentBits(int entityId);

    ComponentMapper getComponentMapper(ComponentType type);

    <T extends Component> T getComponent(int entityId, ComponentType type);

    boolean hasComponent(int entityId, ComponentType type);

    <T extends Component> T addComponent(int entityId, T component);

    <T extends Component> T addComponent(int entityId, ComponentType type, T component);

    void removeComponent(int entityId, ComponentType type);
}
