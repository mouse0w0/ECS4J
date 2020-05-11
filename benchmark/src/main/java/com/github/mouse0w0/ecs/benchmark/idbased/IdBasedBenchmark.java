package com.github.mouse0w0.ecs.benchmark.idbased;

import com.github.mouse0w0.ecs.EntityManager;
import com.github.mouse0w0.ecs.component.ComponentManager;
import com.github.mouse0w0.ecs.system.SystemManager;

public abstract class IdBasedBenchmark {

    protected SystemManager systemManager;

    public void setup(EntityManager entityManager) {
        ComponentManager componentManager = entityManager.getComponentManager();
        int position = componentManager.register(com.github.mouse0w0.ecs.benchmark.idbased.Position.class);
        int velocity = componentManager.register(com.github.mouse0w0.ecs.benchmark.idbased.Velocity.class);
        systemManager = entityManager.getSystemManager();
        systemManager.register(new MoveSystem());
        for (int i = 0; i < 0x1000; i++) {
            int entity = entityManager.createEntity();
            componentManager.addComponent(entity, position, new Position(Math.random(), Math.random(), Math.random()));
            componentManager.addComponent(entity, velocity, new Velocity(Math.random(), Math.random(), Math.random()));
        }
    }
}
