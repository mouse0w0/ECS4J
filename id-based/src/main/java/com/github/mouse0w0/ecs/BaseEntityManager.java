package com.github.mouse0w0.ecs;

import com.github.mouse0w0.ecs.component.ComponentManager;
import com.github.mouse0w0.ecs.system.SystemManager;
import com.github.mouse0w0.ecs.util.BoolArray;
import com.github.mouse0w0.ecs.util.IntIterator;
import com.github.mouse0w0.ecs.util.IntQueue;

import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public abstract class BaseEntityManager implements EntityManager {

    private final BoolArray existingEntities = new BoolArray();
    private final IntQueue recycledEntityId = new IntQueue();

    private final ComponentManager componentManager = createComponentManager();
    private final SystemManager systemManager = createSystemManager();

    private int nextId = 0;

    protected abstract ComponentManager createComponentManager();

    protected abstract SystemManager createSystemManager();

    @Override
    public ComponentManager getComponentManager() {
        return componentManager;
    }

    @Override
    public SystemManager getSystemManager() {
        return systemManager;
    }

    @Override
    public int createEntity() {
        int id;
        if (recycledEntityId.isEmpty()) {
            id = nextId++;
            if (nextId >= existingEntities.size()) {
                existingEntities.ensureCapacity(nextId);
                componentManager.ensureCapacity(nextId);
            }
        } else {
            id = recycledEntityId.pop();
        }
        existingEntities.unsafeMark(id);
        componentManager.onCreatedEntity(id);
        return id;
    }

    @Override
    public boolean isExistingEntity(int entityId) {
        return existingEntities.get(entityId);
    }

    @Override
    public void destroy(int entityId) {
        boolean exists = existingEntities.get(entityId);
        if (exists) {
            existingEntities.clear(entityId);
            recycledEntityId.push(entityId);
            componentManager.onDestroyedEntity(entityId);
        }
    }

    @Override
    public int capacity() {
        return nextId;
    }

    @Override
    public IntIterator getEntities() {
        return new EntityIterator(0, capacity());
    }

    @Override
    public IntIterator getEntities(int first, int count) {
        return new EntityIterator(first, count);
    }

    private final class EntityIterator implements IntIterator {
        private int i;
        private int count;
        private int nextId = -1;

        public EntityIterator(int first, int count) {
            this.i = first;
            this.count = count;
        }

        private void findNext() {
            for (; i < count; i++) {
                if (existingEntities.get(i)) {
                    nextId = i++;
                    return;
                }
            }
            nextId = -1;
        }

        @Override
        public boolean hasNext() {
            findNext();
            return nextId != -1;
        }

        @Override
        public int next() {
            return nextId;
        }
    }
}
