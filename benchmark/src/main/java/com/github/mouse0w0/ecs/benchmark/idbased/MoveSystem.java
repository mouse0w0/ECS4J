package com.github.mouse0w0.ecs.benchmark.idbased;

import com.github.mouse0w0.ecs.benchmark.common.Position;
import com.github.mouse0w0.ecs.benchmark.common.Velocity;
import com.github.mouse0w0.ecs.system.System;

public class MoveSystem {

    @System
    public void onMove(int id, Position position, Velocity velocity) {
        position.x += velocity.x;
        position.y += velocity.y;
        position.z += velocity.z;
    }
}
