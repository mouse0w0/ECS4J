package com.github.mouse0w0.ecs;

import com.github.mouse0w0.ecs.component.ComponentManager;
import com.github.mouse0w0.ecs.util.IntIterator;

public interface EntityManager {

    ComponentManager getComponentManager();

    int createEntity();

    boolean isExistingEntity(int entityId);

    void destroy(int entityId);

    int capacity();

    IntIterator getEntities();

    IntIterator getEntities(int first, int count);
}
