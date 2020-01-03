package com.github.mouse0w0.ecs;

import com.github.mouse0w0.ecs.component.ComponentManager;
import com.github.mouse0w0.ecs.util.BoolArray;
import com.github.mouse0w0.ecs.util.IntIterator;
import com.github.mouse0w0.ecs.util.IntQueue;

public abstract class BaseEntityManager implements EntityManager {

    private final BoolArray existingEntities = new BoolArray();
    private final IntQueue recycledEntityId = new IntQueue();

    private final ComponentManager componentManager = createComponentManager();

    private int nextId = 0;

    protected abstract ComponentManager createComponentManager();

    @Override
    public ComponentManager getComponentManager() {
        return componentManager;
    }

    @Override
    public int createEntity() {
        int id;
        if (recycledEntityId.isEmpty()) {
            if (nextId >= existingEntities.size()) {
                existingEntities.ensureCapacity(nextId);
                componentManager.ensureCapacity(nextId);
            }
            id = nextId++;
        } else {
            id = recycledEntityId.pop();
        }
        existingEntities.unsafeMark(id);
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
        }
    }

    @Override
    public IntIterator getEntities() {
        return new EntityIterator();
    }

    private final class EntityIterator implements IntIterator {
        int i = 0;
        int size = existingEntities.size();
        int nextId = -1;

        private void findNext() {
            for (; i < size; i++) {
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
