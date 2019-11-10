package com.github.mouse0w0.ecs;

import com.github.mouse0w0.ecs.component.Component;
import com.github.mouse0w0.ecs.component.ComponentType;

public interface LowLevelEntityManager extends EntityManager {

    boolean isExistingEntity(int entityId);

    <T extends Component> T getComponent(int entityId, ComponentType type);

    boolean hasComponent(int entityId, ComponentType type);

    <T extends Component> T addComponent(int entityId, T component);

    void removeComponent(int entityId, ComponentType type);

    void destroy(int entityId);
}
