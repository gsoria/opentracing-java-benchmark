package io.opentracing.contrib.benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;

public class BenchmarkSimpleServletSampleTime extends BenchmarkSimpleServletBase {

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
    public String jaegerTracer(StateVariablesJaegerWithoutMetricFilters state)
            throws Exception {
        return super.testSimpleRequest(state);
    }

    //Haystack
    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public String haystackTracer(StateVariablesHaystackWithoutMetricFilters state)
            throws Exception {
        return super.testSimpleRequest(state);
    }

    //Mocktracer
    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public String mockTracer(StateVariablesMockTracerWithoutMetricFilters state)
            throws Exception {
        return super.testSimpleRequest(state);
    }

   //Nooptracer
    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public String noopTracer(StateVariablesNoopTracerWithoutMetricFilters state)
            throws Exception {
        return super.testSimpleRequest(state);
    }
}
