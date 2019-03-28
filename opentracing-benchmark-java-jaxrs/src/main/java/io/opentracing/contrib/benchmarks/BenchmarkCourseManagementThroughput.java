package io.opentracing.contrib.benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;

public class BenchmarkCourseManagementThroughput extends BenchmarkCourseManagement {

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public String benchmarkCourseNotInstrumented(BenchmarkCourseManagement.StateVariablesNotInstrumented state) {
        return getAllCourses(state);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public String benchmarkCourseNoopTracer(BenchmarkCourseManagement.StateVariablesNoopTracer state) {
        return getAllCourses(state);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public String benchmarkCourseJaegerTracer(BenchmarkCourseManagement.StateVariablesJaeger state) {
        return getAllCourses(state);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public String benchmarkCourseHaystackTracer(BenchmarkCourseManagement.StateVariablesHaystack state) {
        return getAllCourses(state);
    }
}


