package com.github.mouse0w0.ecs.component;

public interface ComponentManager {
    <T extends Component> T getComponent(int entityId, ComponentType type);

    boolean hasComponent(int entityId, ComponentType type);

    <T extends Component> T addComponent(int entityId, T component);

    void removeComponent(int entityId, ComponentType type);

    void saveComponent(int entityId, Component component);
}
