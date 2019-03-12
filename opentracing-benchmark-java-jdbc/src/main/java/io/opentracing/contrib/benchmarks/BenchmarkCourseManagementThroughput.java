package io.opentracing.contrib.benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;

import javax.ws.rs.core.Response;

public class BenchmarkCourseManagementThroughput extends BenchmarkCourseManagement {

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public Response benchmarkCourseNotInstrumented(BenchmarkCourseManagement.StateVariablesNotInstrumented state) {
        return getAllCourses(state);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public Response benchmarkCourseNoopTracer(BenchmarkCourseManagement.StateVariablesNoopTracer state) {
        return getAllCourses(state);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public Response benchmarkCourseJaegerTracer(BenchmarkCourseManagement.StateVariablesJaeger state) {
        return getAllCourses(state);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public Response benchmarkCourseHaystackTracer(BenchmarkCourseManagement.StateVariablesHaystack state) {
        return getAllCourses(state);
    }
}


