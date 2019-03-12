package io.opentracing.contrib.benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;

import javax.ws.rs.core.Response;

public class BenchmarkCourseManagementSampleTime extends BenchmarkCourseManagement {

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public Response benchmarkCourseNotInstrumented(StateVariablesNotInstrumented state) {
        return getAllCourses(state);
    }

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public Response benchmarkCourseNoopTracer(StateVariablesNoopTracer state) {
        return getAllCourses(state);
    }

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public Response benchmarkCourseJaegerTracer(StateVariablesJaeger state) {
        return getAllCourses(state);
    }

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public Response benchmarkCourseHaystackTracer(StateVariablesHaystack state) {
        return getAllCourses(state);
    }
}


