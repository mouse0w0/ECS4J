package com.github.mouse0w0.ecs.benchmark.ashley;

import com.badlogic.ashley.core.Component;

public class Position implements Component {

    public double x, y, z;

    public Position(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
