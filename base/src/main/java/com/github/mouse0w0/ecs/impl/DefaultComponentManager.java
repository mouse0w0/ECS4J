package com.github.mouse0w0.ecs.impl;

import com.github.mouse0w0.ecs.component.ComponentTypeFactory;

public class DefaultComponentManager extends BaseComponentManager {
    @Override
    protected ComponentTypeFactory createComponentTypeFactory() {
        return new DefaultComponentTypeFactory();
    }
}
