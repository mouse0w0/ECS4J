package com.github.mouse0w0.ecs.benchmark.idbased;

import com.github.mouse0w0.ecs.component.Component;

public class Velocity implements Component {

    public double x, y, z;

    public Velocity(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
