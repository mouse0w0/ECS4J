package com.github.mouse0w0.ecs.system;

import com.github.mouse0w0.ecs.DefaultEntityManager;

public class MultiThreadEntityManager extends DefaultEntityManager {

    @Override
    protected SystemManager createSystemManager() {
        return new MultiThreadSystemManager(this);
    }
}
