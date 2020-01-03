package com.github.mouse0w0.ecs.component;

import com.github.mouse0w0.ecs.util.ObjectArray;

import java.util.IdentityHashMap;
import java.util.Map;

public class ComponentTypeFactory {

    private final ObjectArray<Class<? extends Component>> idToType = new ObjectArray(Class.class);
    private final Map<Class<? extends Component>, Integer> typeToId = new IdentityHashMap<>();

    private int nextId = 0;

    public int register(Class<? extends Component> clazz) {
        if (typeToId.containsKey(clazz)) throw new IllegalStateException("Cannot register twice component");
        int id = nextId++;
        idToType.set(id, clazz);
        typeToId.put(clazz, id);
        return id;
    }

    public int get(Class<? extends Component> clazz) {
        return typeToId.get(clazz);
    }

    public Class<? extends Component> get(int componentId) {
        return idToType.get(componentId);
    }
}
