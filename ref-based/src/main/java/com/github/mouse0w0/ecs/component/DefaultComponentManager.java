package com.github.mouse0w0.ecs.component;

public class DefaultComponentManager extends BaseComponentManager {
    @Override
    protected ComponentTypeFactory createComponentTypeFactory() {
        return new DefaultComponentTypeFactory();
    }
}
