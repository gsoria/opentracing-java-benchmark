package io.opentracing.contrib.benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;

import javax.ws.rs.core.Response;

public class BenchmarkCourseManagementSampleTime extends BenchmarkCourseManagementBase {

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public Response noInstrumentation(StateVariablesNotInstrumented state) {
        return getAllCourses(state);
    }

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public Response noopTracer(StateVariablesNoopTracer state) {
        return getAllCourses(state);
    }

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public Response jaegerTracer(StateVariablesJaeger state) {
        return getAllCourses(state);
    }

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public Response haystackTracer(StateVariablesHaystack state) {
        return getAllCourses(state);
    }

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public Response mockTracer(StateVariablesMockTracer state) {
        return getAllCourses(state);
    }
}


