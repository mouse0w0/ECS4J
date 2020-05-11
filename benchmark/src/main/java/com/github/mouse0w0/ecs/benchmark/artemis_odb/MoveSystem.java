package com.github.mouse0w0.ecs.benchmark.artemis_odb;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;

@All({Position.class, Velocity.class})
public class MoveSystem extends IteratingSystem {

    protected ComponentMapper<Position> positionMapper;
    protected ComponentMapper<Velocity> velocityMapper;

    @Override
    protected void process(int entityId) {
        Position position = positionMapper.get(entityId);
        Velocity velocity = velocityMapper.get(entityId);
        position.x += velocity.x;
        position.y += velocity.y;
        position.z += velocity.z;
    }
}
