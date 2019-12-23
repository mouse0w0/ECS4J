package com.github.mouse0w0.ecs;

import com.github.mouse0w0.ecs.component.Component;
import com.github.mouse0w0.ecs.component.ComponentManager;
import com.github.mouse0w0.ecs.component.ComponentType;
import com.github.mouse0w0.ecs.util.IntQueue;
import com.github.mouse0w0.ecs.util.ObjectArray;

import java.util.HashSet;
import java.util.Set;

public abstract class BaseEntityManager implements LowLevelEntityManager {

    private final ObjectArray<EntityRef> existingEntities = new ObjectArray<>();
    private final Set<EntityRef> entities = new HashSet<>();
    private final IntQueue recycledEntityId = new IntQueue();

    private final ComponentManager componentManager = createComponentManager();

    private int nextId = 0;

    protected abstract ComponentManager createComponentManager();

    @Override
    public EntityRef createEntity() {
        EntityRef ref;
        if (recycledEntityId.isEmpty()) {
            int id = nextId++;
            ref = new BaseEntityRef(this, id);
            existingEntities.set(id, ref);
        } else {
            int id = recycledEntityId.pop();
            ref = new BaseEntityRef(this, id);
            existingEntities.unsafeSet(id, ref);
        }
        entities.add(ref);
        return ref;
    }

    @Override
    public EntityRef getEntity(int entityId) {
        return existingEntities.get(entityId);
    }

    @Override
    public Iterable<EntityRef> getEntities() {
        return entities;
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
            entities.remove(entityRef);
            recycledEntityId.push(entityId);
        }
    }
}
