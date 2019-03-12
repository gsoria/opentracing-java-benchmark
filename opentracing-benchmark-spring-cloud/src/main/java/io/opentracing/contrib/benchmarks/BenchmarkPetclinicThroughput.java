package io.opentracing.contrib.benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.springframework.samples.petclinic.owner.Owner;

public class BenchmarkPetclinicThroughput extends BenchmarkPetclinic {

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public Owner benchmarkBillingNotInstrumented(StateVariablesNotInstrumented state) {
        return findPetById(state);
    }

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public Owner benchmarkBillingNoopTracer(StateVariablesNoopTracer state) {
        return findPetById(state);
    }

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public Owner benchmarkBillingJaegerTracer(StateVariablesJaeger state) {
        return findPetById(state);
    }

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public Owner benchmarkBillingHaystackTracer(StateVariablesHaystack state) {
        return findPetById(state);
    }
}


