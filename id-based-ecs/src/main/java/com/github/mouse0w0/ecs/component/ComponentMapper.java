package com.github.mouse0w0.ecs.component;

import com.github.mouse0w0.ecs.util.ObjectArray;

public class ComponentMapper {
    private final int id;
    private final Class<? extends Component> type;
    private final ObjectArray<Component> components;

    public ComponentMapper(int id, Class<? extends Component> type) {
        this.id = id;
        this.type = type;
        this.components = new ObjectArray(this.type);
    }

    public int getId() {
        return id;
    }

    public Class<? extends Component> getType() {
        return type;
    }

    public Component get(int entityId) {
        return components.get(entityId);
    }

    public void set(int entityId, Component value) {
        components.unsafeSet(entityId, value);
    }

    public void ensureCapacity(int minCapacity) {
        components.ensureCapacity(minCapacity);
    }
}
