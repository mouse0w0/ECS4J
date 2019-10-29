package com.github.mouse0w0.ecs.component;

public interface ComponentContainer extends ReadOnlyComponentContainer {

    <T extends Component> T addComponent(T component);

    void removeComponent(ComponentType type);

    void saveComponent(Component component);
}
