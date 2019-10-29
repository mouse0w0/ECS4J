package com.github.mouse0w0.ecs;

public interface EntityManager {

    EntityRef createEntity();

    EntityRef getEntity(int entityId);
}
