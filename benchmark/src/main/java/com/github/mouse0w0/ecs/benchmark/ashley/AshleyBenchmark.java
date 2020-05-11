package com.github.mouse0w0.ecs.benchmark.ashley;

import com.badlogic.ashley.core.Engine;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 3, time = 5)
@Measurement(iterations = 10, time = 5)
@Threads(1)
@Fork(1)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Thread)
public class AshleyBenchmark {

    private Engine engine;

    @Setup
    public void setup() {
        engine = new Engine();
        engine.addSystem(new MoveSystem());
        for (int i = 0; i < 0x1000; i++) {
            engine.addEntity(engine.createEntity()
                    .add(new Position(Math.random(), Math.random(), Math.random()))
                    .add(new Velocity(Math.random(), Math.random(), Math.random())));
        }
    }

    @Benchmark
    public void testUpdate() {
        engine.update(0);
    }
}
