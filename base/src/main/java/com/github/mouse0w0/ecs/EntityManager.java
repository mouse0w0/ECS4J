package com.github.mouse0w0.ecs;

import com.github.mouse0w0.ecs.component.Component;
import com.github.mouse0w0.ecs.component.ComponentType;

public interface EntityManager {

    EntityRef createEntity();

    EntityRef getEntity(int entityId);

    boolean isExistingEntity(int entityId);

    void destroy(int entityId);

    Iterable<EntityRef> getEntities();

    <T extends Component> T getComponent(int id, ComponentType type);

    boolean hasComponent(int id, ComponentType type);

    <T extends Component> T addComponent(int id, T component);

    void removeComponent(int id, ComponentType type);
}
