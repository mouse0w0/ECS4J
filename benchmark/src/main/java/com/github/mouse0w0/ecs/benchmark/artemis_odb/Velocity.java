package com.github.mouse0w0.ecs.benchmark.artemis_odb;

import com.artemis.Component;

public class Velocity extends Component {

    public double x, y, z;

    public Velocity() {
    }

    public Velocity(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
