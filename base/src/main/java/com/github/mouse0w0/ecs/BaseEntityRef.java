package com.github.mouse0w0.ecs;

import com.github.mouse0w0.ecs.component.Component;
import com.github.mouse0w0.ecs.component.ComponentType;

public class BaseEntityRef implements EntityRef {

    private final EntityManager entityManager;

    private int id;

    protected BaseEntityRef(EntityManager entityManager, int id) {
        this.entityManager = entityManager;
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean isExists() {
        return id != -1;
    }

    @Override
    public synchronized void destroy() {
        if (isExists()) {
            entityManager.destroy(id);
            id = -1;
        }
    }

    @Override
    public <T extends Component> T addComponent(T component) {
        return entityManager.addComponent(id, component);
    }

    @Override
    public void removeComponent(ComponentType type) {
        entityManager.removeComponent(id, type);
    }

    @Override
    public <T extends Component> T getComponent(ComponentType type) {
        return entityManager.getComponent(id, type);
    }

    @Override
    public boolean hasComponent(ComponentType type) {
        return entityManager.hasComponent(id, type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntityRef that = (BaseEntityRef) o;
        return id == that.id && entityManager == that.entityManager;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
