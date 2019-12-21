package com.github.mouse0w0.ecs;

import java.util.Random;

public class MovePerformanceTest {

    static Position[] positions;
    static Velocity[] velocities;

    public static void main(String[] args) {
        init();
        long start = System.nanoTime();
        for (int i = 0; i < 32768; i++) {
            if (positions[i] != null && velocities[i] != null) {
                positions[i].x += velocities[i].dx;
                positions[i].y += velocities[i].dy;
                positions[i].z += velocities[i].dz;
            }
        }
        System.out.println(System.nanoTime() - start);
    }

    public static void init() {
        positions = new Position[32768];
        velocities = new Velocity[32768];
        Random random = new Random();
        for (int i = 0; i < 32768; i++) {
            positions[random.nextInt(32768)] = new Position(random.nextDouble(), random.nextDouble(), random.nextDouble());
            velocities[random.nextInt(32768)] = new Velocity(random.nextDouble(), random.nextDouble(), random.nextDouble());
        }
    }

    public static class Position {
        double x, y, z;

        public Position(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    public static class Velocity {
        double dx, dy, dz;

        public Velocity(double dx, double dy, double dz) {
            this.dx = dx;
            this.dy = dy;
            this.dz = dz;
        }
    }
}
