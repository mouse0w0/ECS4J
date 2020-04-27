package com.github.mouse0w0.ecs.benchmark.idbased;

import com.github.mouse0w0.ecs.DefaultEntityManager;
import com.github.mouse0w0.ecs.EntityManager;
import com.github.mouse0w0.ecs.benchmark.common.Position;
import com.github.mouse0w0.ecs.benchmark.common.Velocity;
import com.github.mouse0w0.ecs.component.ComponentManager;
import com.github.mouse0w0.ecs.system.DefaultSystemManager;
import com.github.mouse0w0.ecs.system.SystemManager;
import com.github.mouse0w0.ecs.system.invoker.AsmSystemInvokerFactory;
import com.github.mouse0w0.ecs.system.invoker.SystemInvokerFactory;

class SystemUpdateTest {

    public static void main(String[] args) {
        test(new DefaultEntityManager());
//        test(new MultiThreadEntityManager());
        benchmark.artemis_odb.Main.main(new String[0]);
        test(new DefaultEntityManager() {
            @Override
            protected SystemManager createSystemManager() {
                return new DefaultSystemManager(this) {
                    @Override
                    protected SystemInvokerFactory createInvokerFactory() {
                        return new AsmSystemInvokerFactory();
                    }
                };
            }
        });
//        test(new MultiThreadEntityManager() {
//            @Override
//            protected SystemManager createSystemManager() {
//                return new MultiThreadSystemManager(this) {
//                    @Override
//                    protected SystemInvokerFactory createInvokerFactory() {
//                        return new AsmSystemInvokerFactory();
//                    }
//                };
//            }
//        });
    }

    public static void test(EntityManager entityManager) {
        ComponentManager componentManager = entityManager.getComponentManager();
        int position = componentManager.register(Position.class);
        int velocity = componentManager.register(Velocity.class);
        SystemManager systemManager = entityManager.getSystemManager();
        systemManager.register(new MoveSystem());
        for (int i = 0; i < 0x10000; i++) {
            int entity = entityManager.createEntity();
            componentManager.addComponent(entity, position, new Position(Math.random(), Math.random(), Math.random()));
            componentManager.addComponent(entity, velocity, new Velocity(Math.random(), Math.random(), Math.random()));
        }
        System.out.println("Generated entities.");
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
