package io.opentracing.contrib.benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;

public class BenchmarkStringConcatenationSampleTime extends BenchmarkStringConcatenation {

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public String testNoInstrumentation(StateVariables state) {
        return doNoInstrumentation(state);
    }

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public void testNoopTracer(StateVariables state) {
        doNoopTracer(state);
    }

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public void testMockTracer(StateVariables state) {
        doMockTracer(state);
    }

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public void testJaegerTracer(StateVariables state) {
        doJaegerTracer(state);
    }

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public void testHaystackTracer(StateVariables state) {
        doHaystackTracer(state);
    }
}


