package com.github.mouse0w0.ecs.benchmark.artemis_odb;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 3, time = 5)
@Measurement(iterations = 10, time = 5)
@Threads(24)
@Fork(1)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Thread)
public class MultiThreadArtemisBenchmark {

    private World world;

    @Setup
    public void setup() {
        WorldConfiguration configuration = new WorldConfigurationBuilder()
                .with(new MultiThreadMoveSystem()).build();

        world = new World(configuration);

        for (int i = 0; i < 0x1000; i++) {
            int entityId = world.create();
            world.edit(entityId)
                    .add(new Position(Math.random(), Math.random(), Math.random()))
                    .add(new Velocity(Math.random(), Math.random(), Math.random()));
        }
    }

    @Benchmark
    public void testProcess() {
        world.process();
    }
}
