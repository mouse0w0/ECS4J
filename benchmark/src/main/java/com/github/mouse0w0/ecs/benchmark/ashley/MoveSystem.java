package com.github.mouse0w0.ecs.benchmark.ashley;


import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

public class MoveSystem extends IteratingSystem {

    protected ComponentMapper<Position> positionMapper = ComponentMapper.getFor(Position.class);
    protected ComponentMapper<Velocity> velocityMapper = ComponentMapper.getFor(Velocity.class);

    public MoveSystem() {
        super(Family.all(Position.class, Velocity.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Position position = positionMapper.get(entity);
        Velocity velocity = velocityMapper.get(entity);
        position.x += velocity.x;
        position.y += velocity.y;
        position.z += velocity.z;
    }
}
