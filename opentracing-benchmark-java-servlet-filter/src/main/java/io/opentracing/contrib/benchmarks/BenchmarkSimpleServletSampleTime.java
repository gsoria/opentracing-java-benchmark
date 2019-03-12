package io.opentracing.contrib.benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;

public class BenchmarkSimpleServletSampleTime
        extends BenchmarkSimpleServlet {

    //Not instrumented
    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public String testSimpleRequest(StateVariablesNotInstrumented state)
            throws Exception {
        return super.testSimpleRequest(state);
    }

    //Jaeger
    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public String testSimpleRequestJaegerWithoutMetricFilter(StateVariablesJaegerWithoutMetricFilters state)
            throws Exception {
        return super.testSimpleRequest(state);
    }

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public String testSimpleRequestJaegerWithMetricFilter(StateVariablesJaegerWithMetricFilters state)
            throws Exception {
        return super.testSimpleRequest(state);
    }

    //Haystack
    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public String testSimpleRequestHaystackWithoutMetricFilter(StateVariablesHaystackWithoutMetricFilters state)
            throws Exception {
        return super.testSimpleRequest(state);
    }

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public String testSimpleRequestHaystackWithMetricFilter(StateVariablesHaystackWithMetricFilters state)
            throws Exception {
        return super.testSimpleRequest(state);
    }

    //Mocktracer
    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public String testSimpleRequestMocktracerWithoutMetricFilter(StateVariablesMockTracerWithoutMetricFilters state)
            throws Exception {
        return super.testSimpleRequest(state);
    }

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public String testSimpleRequestMocktracerWithMetricFilter(StateVariablesMockTracerWithMetricFilters state)
            throws Exception {
        return super.testSimpleRequest(state);
    }

    //Nooptracer
    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public String testSimpleRequestNooptracerWithoutMetricFilter(StateVariablesNoopTracerWithoutMetricFilters state)
            throws Exception {
        return super.testSimpleRequest(state);
    }

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public String testSimpleRequestNooptracerWithMetricFilter(StateVariablesNoopTracerWithMetricFilters state)
            throws Exception {
        return super.testSimpleRequest(state);
    }
}
