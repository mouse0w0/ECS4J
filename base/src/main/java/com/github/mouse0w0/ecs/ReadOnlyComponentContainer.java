package com.github.mouse0w0.ecs;

public interface ReadOnlyComponentContainer {

    boolean hasComponent(Class<? extends Component> component);

    <T extends Component> T getComponent(ComponentType type);
}
