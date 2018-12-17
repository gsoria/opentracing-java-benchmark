package org.sample.benchmarks;

import io.opentracing.Tracer;
import io.opentracing.noop.NoopTracerFactory;
import org.openjdk.jmh.annotations.*;
import org.sample.benchmarks.service.StringConcatenationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.sample.benchmarks")
public class StringConcatenationOpentracingNoopTracer {

    /*
    Sometimes you way want to initialize some variables that your benchmark code needs,
    but which you do not want to be part of the code your benchmark measures.
    Such variables are called "state" variables.
    */

    @State(Scope.Thread)
    public static class StateVariables {

        @Autowired
        StringConcatenationService service;

        String a = "Hello ";
        String b = "world";

        //Tell JMH that this method should be called to setup the state object before it
        //is passed to the benchmark method.
        @Setup(Level.Trial)
        public void doSetup() {
            System.out.println("Do Setup");
            SpringApplication.run(StringConcatenationOpentracingNoopTracer .class);
        }

        //Tells JMH that this method should be called to clean up ("tear down") the state
        //object after the benchmark has been execute
        @TearDown(Level.Trial)
        public void doTearDown() {
            System.out.println("Do TearDown");
        }

        //https://github.com/opentracing/opentracing-java/tree/master/opentracing-noop
        Tracer tracer = NoopTracerFactory.create();
    }

    @Benchmark
    public void testPlusConcatenation(StateVariables state) {
        io.opentracing.Scope scope = state.tracer.buildSpan("testPlusConcatenation").startActive(true);
        state.service.testPlusConcatenation(state.a, state.b);
        scope.span().log("testPlusConcatenation");
    }

    @Benchmark
    public void testStringConcatenationStringBuilder(StateVariables state) {
        io.opentracing.Scope scope = state.tracer.buildSpan("testStringConcatenationStringBuilder").startActive(true);
        state.service.testStringConcatenationStringBuilder(state.a, state.b);
        scope.span().log("testStringConcatenationStringBuilder");
    }

    @Benchmark
    public void testStringConcatenationStringBuffer(StateVariables state) {
        io.opentracing.Scope scope = state.tracer.buildSpan("testStringConcatenationStringBuffer").startActive(true);
        state.service.testStringConcatenationStringBuffer(state.a, state.b);
        scope.span().log("testStringConcatenationStringBuffer");
    }
}


