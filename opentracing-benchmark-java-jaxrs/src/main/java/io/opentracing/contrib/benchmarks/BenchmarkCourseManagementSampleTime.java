package io.opentracing.contrib.benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;

public class BenchmarkCourseManagementSampleTime extends BenchmarkCourseManagement {

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public String benchmarkCourseNotInstrumented(StateVariablesNotInstrumented state) {
        return getAllCourses(state);
    }

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public String benchmarkCourseNoopTracer(StateVariablesNoopTracer state) {
        return getAllCourses(state);
    }

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public String benchmarkCourseJaegerTracer(StateVariablesJaeger state) {
        return getAllCourses(state);
    }

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public String benchmarkCourseHaystackTracer(StateVariablesHaystack state) {
        return getAllCourses(state);
    }
}


