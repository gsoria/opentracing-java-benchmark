package io.opentracing.contrib.benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import io.opentracing.contrib.benchmarks.petclinic.owner.Owner;

public class BenchmarkPetclinicSampleTime extends BenchmarkPetclinicBase {

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public Owner noInstrumentation(StateVariablesNoInstrumentation state) {
        return findPetOwnerById(state);
    }

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public Owner noopTracer(StateVariablesNoopTracer state) {
        return findPetOwnerById(state);
    }

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public Owner jaegerTracer(StateVariablesJaeger state) {
        return findPetOwnerById(state);
    }

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public Owner haystackTracer(StateVariablesHaystack state) {
        return findPetOwnerById(state);
    }

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    public Owner mockTracer(StateVariablesMockTracer state) {
        return findPetOwnerById(state);
    }
}


