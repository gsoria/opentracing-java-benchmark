package org.sample.benchmarks;

import io.opentracing.Tracer;
import io.opentracing.noop.NoopTracerFactory;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

public class StringConcatenationOpentracingNoopTracer {

    /*
    Sometimes you way want to initialize some variables that your benchmark code needs,
    but which you do not want to be part of the code your benchmark measures.
    Such variables are called "state" variables.
    */

    @State(Scope.Thread)
    public static class StateVariables {
        String a = "Hello ";
        String b = "world";

        //https://github.com/opentracing/opentracing-java/tree/master/opentracing-noop
        Tracer tracer = NoopTracerFactory.create();
    }

    @Benchmark
    public void testPlusConcatenation(StateVariables state) {
        try (io.opentracing.Scope scope = state.tracer.buildSpan("testPlusConcatenation").startActive(true)) {
            String c = state.a + state.b;
            scope.span().log("testPlusConcatenation");
        }
    }

    @Benchmark
    public void testStringConcatenationStringBuilder(StateVariables state) {
        try (io.opentracing.Scope scope = state.tracer.buildSpan("testStringConcatenationStringBuilder").startActive(true)) {
            StringBuilder sb = new StringBuilder();
            sb.append(state.a).append(state.b).toString();
            scope.span().log("testStringConcatenationStringBuilder");
        }
    }

    @Benchmark
    public void testStringConcatenationStringBuffer(StateVariables state) {
        try (io.opentracing.Scope scope = state.tracer.buildSpan("testStringConcatenationStringBuffer").startActive(true)) {
            StringBuffer sb = new StringBuffer();
            sb.append(state.a).append(state.b).toString();
            scope.span().log("testStringConcatenationStringBuffer");
        }
    }
}


