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
    public void testPlusConcatenation(StateVariables state) {
        String c = state.a + state.b;
    }

    @Benchmark
    public void testStringConcatenationStringBuilder(StateVariables state) {
        StringBuilder sb = new StringBuilder();
        sb.append(state.a).append(state.b).toString();
    }

    @Benchmark
    public void testStringConcatenationStringBuffer(StateVariables state) {
        StringBuffer sb = new StringBuffer();
        sb.append(state.a).append(state.b).toString();
    }
}


