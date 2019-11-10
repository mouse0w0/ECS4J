package com.github.mouse0w0.ecs.impl;

import com.github.mouse0w0.ecs.EntityRef;
import com.github.mouse0w0.ecs.LowLevelEntityManager;
import com.github.mouse0w0.ecs.component.Component;
import com.github.mouse0w0.ecs.component.ComponentManager;
import com.github.mouse0w0.ecs.component.ComponentType;
import com.github.mouse0w0.ecs.util.IntQueue;
import com.github.mouse0w0.ecs.util.ObjectArray;

import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public abstract class BaseThreadSafeEntityManager implements LowLevelEntityManager {

    private final ObjectArray<EntityRef> existingEntities = new ObjectArray<>();
    private final IntQueue recycledEntityId = new IntQueue();

    private final ComponentManager componentManager = createComponentManager();

    private int nextId = 0;

    protected abstract ComponentManager createComponentManager();

    @Override
    public EntityRef createEntity() {
        synchronized (recycledEntityId) {
            if (recycledEntityId.isEmpty()) {
                int id = nextId++;
                EntityRef ref = new BaseEntityRef(this, id);
                synchronized (existingEntities) {
                    existingEntities.set(id, ref);
                }
                return ref;
            } else {
                int id = recycledEntityId.pop();
                EntityRef ref = new BaseEntityRef(this, id);
                existingEntities.unsafeSet(id, ref);
                return ref;
            }
        }
    }

    @Override
    public EntityRef getEntity(int entityId) {
        return existingEntities.get(entityId);
    }

    @Override
    public boolean isExistingEntity(int entityId) {
        return existingEntities.get(entityId) != null;
    }

    @Override
    public <T extends Component> T getComponent(int entityId, ComponentType type) {
        return componentManager.getComponent(entityId, type);
    }

    @Override
    public boolean hasComponent(int entityId, ComponentType type) {
        return componentManager.hasComponent(entityId, type);
    }

    @Override
    public <T extends Component> T addComponent(int entityId, T component) {
        return componentManager.addComponent(entityId, component);
    }

    @Override
    public void removeComponent(int entityId, ComponentType type) {
        componentManager.removeComponent(entityId, type);
    }

    @Override
    public void destroy(int entityId) {
        EntityRef entityRef = existingEntities.get(entityId);
        if (entityRef != null) {
            existingEntities.unsafeSet(entityId, null);
            recycledEntityId.push(entityId);
        }
    }
}
