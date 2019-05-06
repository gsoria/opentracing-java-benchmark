package io.opentracing.contrib.benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;

public class BenchmarkCourseManagementThroughput extends BenchmarkCourseManagementBase {

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public String noInstrumentation(StateVariablesNotInstrumented state) {
        return getAllCourses(state);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public String noopTracer(StateVariablesNoopTracer state) {
        return getAllCourses(state);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public String jaegerTracer(StateVariablesJaeger state) {
        return getAllCourses(state);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public String haystackTracer(StateVariablesHaystack state) {
        return getAllCourses(state);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public String mockTracer(StateVariablesMockTracer state) {
        return getAllCourses(state);
    }
}


