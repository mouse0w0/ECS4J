package com.github.mouse0w0.ecs.benchmark.idbased;

import com.github.mouse0w0.ecs.DefaultEntityManager;
import com.github.mouse0w0.ecs.system.MultiThreadSystemManager;
import com.github.mouse0w0.ecs.system.SystemManager;
import com.github.mouse0w0.ecs.system.invoker.AsmSystemInvokerFactory;
import com.github.mouse0w0.ecs.system.invoker.SystemInvokerFactory;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 3, time = 5)
@Measurement(iterations = 10, time = 5)
@Threads(24)
@Fork(1)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Thread)
public class MultiThreadAsmIdBasedBenchmark extends IdBasedBenchmark {

    @Setup
    public void setup() {
        setup(new DefaultEntityManager() {
            @Override
            protected SystemManager createSystemManager() {
                return new MultiThreadSystemManager(this) {
                    @Override
                    protected SystemInvokerFactory createInvokerFactory() {
                        return new AsmSystemInvokerFactory();
                    }
                };
            }
        });
    }

    @Benchmark
    public void testUpdate() {
        systemManager.update();
    }
}
