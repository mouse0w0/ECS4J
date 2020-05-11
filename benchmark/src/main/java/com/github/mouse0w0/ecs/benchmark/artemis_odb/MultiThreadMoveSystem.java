package com.github.mouse0w0.ecs.benchmark.artemis_odb;

import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.utils.IntBag;

import java.util.concurrent.CompletableFuture;

@All({Position.class, Velocity.class})
public class MultiThreadMoveSystem extends BaseEntitySystem {

    protected ComponentMapper<Position> positionMapper;
    protected ComponentMapper<Velocity> velocityMapper;

    private static final int THREAD_COUNT = 12;

    @Override
    protected void processSystem() {
        IntBag entities = subscription.getEntities();
        int[] data = entities.getData();
        int eachThreadEntityCount = data.length / THREAD_COUNT;
        var futures = new CompletableFuture[THREAD_COUNT];
        for (int i = 0, size = THREAD_COUNT - 1; i < size; i++) {
            int start = i * eachThreadEntityCount;
            int end = start + eachThreadEntityCount;
            futures[i] = CompletableFuture.runAsync(() -> {
                for (int j = start; j < end; j++) process(j);
            });
        }
        int start = (THREAD_COUNT - 1) * eachThreadEntityCount;
        int end = data.length - start;
        futures[THREAD_COUNT - 1] = CompletableFuture.runAsync(() -> {
            for (int j = start; j < end; j++) process(j);
        });
        CompletableFuture.allOf(futures).join();
//        IntStream.of(data).parallel().forEach(this::process);
    }

    protected void process(int entityId) {
        Position position = positionMapper.get(entityId);
        Velocity velocity = velocityMapper.get(entityId);
        position.x += velocity.x;
        position.y += velocity.y;
        position.z += velocity.z;
    }
}
