package com.github.mouse0w0.ecs.benchmark.idbased;

import com.github.mouse0w0.ecs.DefaultEntityManager;
import com.github.mouse0w0.ecs.EntityManager;
import com.github.mouse0w0.ecs.benchmark.common.Position;
import com.github.mouse0w0.ecs.benchmark.common.Velocity;
import com.github.mouse0w0.ecs.component.ComponentManager;
import com.github.mouse0w0.ecs.system.SystemManager;

class SystemManagerTest {

    public static void main(String[] args) {
        EntityManager entityManager = new DefaultEntityManager();
        ComponentManager componentManager = entityManager.getComponentManager();
        int position = componentManager.register(Position.class);
        int velocity = componentManager.register(Velocity.class);
        SystemManager systemManager = entityManager.getSystemManager();
        systemManager.register(new MoveSystem());
        for (int i = 0; i < 0x1000; i++) {
            int entity = entityManager.createEntity();
            componentManager.addComponent(entity, position, new Position(0, 0, 0));
            componentManager.addComponent(entity, velocity, new Velocity(0, 0, 0));
        }
        int count = 0;
        double totalTime = 0;
        while (count < 10000) {
            long startTime = System.nanoTime();
            systemManager.update();
            totalTime += (System.nanoTime() - startTime) / 1e6;
            count++;
        }
        System.out.println(count + ":" + totalTime / count);
    }
}
