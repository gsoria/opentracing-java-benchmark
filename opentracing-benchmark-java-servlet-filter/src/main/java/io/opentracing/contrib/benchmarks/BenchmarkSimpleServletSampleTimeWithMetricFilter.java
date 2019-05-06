package io.opentracing.contrib.benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;

public class BenchmarkSimpleServletSampleTimeWithMetricFilter extends BenchmarkSimpleServletBase {

    //Not instrumented
    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public String noInstrumentation(StateVariablesNoInstrumentation state)
            throws Exception {
        return super.testSimpleRequest(state);
    }

    //Jaeger
    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public String jaegerTracer(StateVariablesJaegerWithMetricFilters state)
            throws Exception {
        return super.testSimpleRequest(state);
    }

    //Haystack
    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public String haystackTracer(StateVariablesHaystackWithMetricFilters state)
            throws Exception {
        return super.testSimpleRequest(state);
    }

    //Mocktracer
    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public String mockTracer(StateVariablesMockTracerWithMetricFilters state)
            throws Exception {
        return super.testSimpleRequest(state);
    }

    //Nooptracer
    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public String noopTracer(StateVariablesNoopTracerWithMetricFilters state)
            throws Exception {
        return super.testSimpleRequest(state);
    }
}
