package io.opentracing.contrib.benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import io.opentracing.contrib.benchmarks.billing.model.Invoice;

public class BenchmarkBillingSampleTime extends BenchmarkBillingBase {

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public Invoice noInstrumentation(StateVariablesNoInstrumentation state) {
        return doBenchmarkBillingNoInstrumentation(state);
    }

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public Invoice noopTracer(StateVariablesNoopTracer state) {
        return doBenchmarkBillingNoopTracer(state);
    }

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public Invoice jaegerTracer(StateVariablesJaeger state) {
        return doBenchmarkBillingJaegerTracer(state);
    }

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public Invoice haystackTracer(StateVariablesHaystack state) {
        return doBenchmarkBillingHaystackTracer(state);
    }

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public Invoice mockTracer(StateVariablesMockTracer state) {
        return doBenchmarkBillingMockTracer(state);
    }
}


