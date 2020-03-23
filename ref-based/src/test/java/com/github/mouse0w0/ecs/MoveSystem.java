package com.github.mouse0w0.ecs;

import com.github.mouse0w0.ecs.system.System;

public class MoveSystem {

    @System
    public void onMove(EntityRef ref, Position position, Velocity velocity) {
        position.x += velocity.x;
        position.y += velocity.y;
        position.z += velocity.z;
    }
}
