package com.github.mouse0w0.ecs;

import com.github.mouse0w0.ecs.component.ComponentManager;

public class DefaultThreadSafeEntityManager extends BaseThreadSafeEntityManager {
    @Override
    protected ComponentManager createComponentManager() {
        return new DefaultComponentManager();
    }
}
