package com.github.mouse0w0.ecs;

import com.github.mouse0w0.ecs.component.ComponentManager;
import com.github.mouse0w0.ecs.component.DefaultComponentManager;
import com.github.mouse0w0.ecs.system.DefaultSystemManager;
import com.github.mouse0w0.ecs.system.SystemManager;

import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public class DefaultEntityManager extends BaseEntityManager {
    @Override
    protected ComponentManager createComponentManager() {
        return new DefaultComponentManager();
    }

    @Override
    protected SystemManager createSystemManager() {
        return new DefaultSystemManager(this);
    }
}
