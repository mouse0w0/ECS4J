package com.github.mouse0w0.ecs.component;

public interface ReadOnlyComponentContainer {

    <T extends Component> T getComponent(ComponentType type);

    boolean hasComponent(ComponentType type);
}
