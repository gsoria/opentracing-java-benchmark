package org.sample.benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

public class StringConcatenation {

    /*
    Sometimes you way want to initialize some variables that your benchmark code needs,
    but which you do not want to be part of the code your benchmark measures.
    Such variables are called "state" variables.
    */

    @State(Scope.Thread)
    public static class StateVariables {
        String a = "Hello ";
        String b = "world";
    }

    @Benchmark
    public String testPlusConcatenation(StateVariables state) {
        return state.a + state.b;
    }

    @Benchmark
    public String testStringConcatenationStringBuilder(StateVariables state) {
        StringBuilder sb = new StringBuilder();
        return sb.append(state.a).append(state.b).toString();
    }

    @Benchmark
    public String testStringConcatenationStringBuffer(StateVariables state) {
        StringBuffer sb = new StringBuffer();
        return sb.append(state.a).append(state.b).toString();
    }
}


