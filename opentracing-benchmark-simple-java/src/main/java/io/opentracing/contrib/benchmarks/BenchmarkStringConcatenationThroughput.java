package io.opentracing.contrib.benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;

public class BenchmarkStringConcatenationThroughput extends BenchmarkStringConcatenationBase {

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public String noInstrumentation(StateVariables state) {
        return doNoInstrumentation(state);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public String noopTracer(StateVariables state) {
        return doNoopTracer(state);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public String mockTracer(StateVariables state) {
        return doMockTracer(state);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public String jaegerTracer(StateVariables state) {
        return doJaegerTracer(state);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public String haystackTracer(StateVariables state) {
        return doHaystackTracer(state);
    }
}
