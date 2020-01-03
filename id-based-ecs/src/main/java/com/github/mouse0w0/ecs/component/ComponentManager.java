package com.github.mouse0w0.ecs.component;

import com.github.mouse0w0.ecs.util.BitArray;

public interface ComponentManager {
    BitArray getComponentBits(int entityId);

    ComponentMapper getComponentMapper(int componentId);

    <T extends Component> T getComponent(int entityId, int componentId);

    boolean hasComponent(int entityId, int componentId);

    <T extends Component> T addComponent(int entityId, int componentId, T component);

    void removeComponent(int entityId, int componentId);

    void ensureCapacity(int minCapability);
}
