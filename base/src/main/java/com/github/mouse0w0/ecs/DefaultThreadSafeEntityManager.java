package com.github.mouse0w0.ecs;

import com.github.mouse0w0.ecs.component.ComponentManager;
import com.github.mouse0w0.ecs.component.DefaultComponentManager;

public class DefaultThreadSafeEntityManager extends BaseThreadSafeEntityManager {
    @Override
    protected ComponentManager createComponentManager() {
        return new DefaultComponentManager();
    }
}
