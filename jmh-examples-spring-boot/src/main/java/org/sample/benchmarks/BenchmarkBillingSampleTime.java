package org.sample.benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.sample.billing.model.Invoice;

public class BenchmarkBillingSampleTime extends BenchmarkBilling {

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public Invoice benchmarkBillingNotInstrumented(StateVariables state) {
        return doBenchmarkBillingNotInstrumented(state);
    }

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public Invoice benchmarkBillingNoopTracer(StateVariablesNoopTracer state) {
        return doBenchmarkBillingNoopTracer(state);
    }

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public Invoice benchmarkBillingJaegerTracer(StateVariablesJaeger state) {
        return doBenchmarkBillingJaegerTracer(state);
    }

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public Invoice benchmarkBillingHaystackTracer(StateVariablesHaystack state) {
        return doBenchmarkBillingHaystackTracer(state);
    }
}


