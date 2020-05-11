package com.github.mouse0w0.ecs.benchmark;

import com.github.mouse0w0.ecs.benchmark.artemis_odb.ArtemisBenchmark;
import com.github.mouse0w0.ecs.benchmark.ashley.AshleyBenchmark;
import com.github.mouse0w0.ecs.benchmark.idbased.AsmIdBasedBenchmark;
import com.github.mouse0w0.ecs.benchmark.idbased.MultiThreadAsmIdBasedBenchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class Benchmark {
    public static void main(String[] args) throws RunnerException {
        new Runner(new OptionsBuilder()
                .output("benchmark.log")
                .include(ArtemisBenchmark.class.getSimpleName())
                .include(AshleyBenchmark.class.getSimpleName())
                .include(AsmIdBasedBenchmark.class.getSimpleName())
                .include(MultiThreadAsmIdBasedBenchmark.class.getSimpleName())
                .build()).run();
    }
}
