package org.sample.benchmarks;

import io.opentracing.Tracer;
import io.opentracing.noop.NoopTracerFactory;
import org.openjdk.jmh.annotations.*;
import org.sample.benchmarks.service.StringConcatenationService;
import org.sample.benchmarks.service.impl.StringConcatenationServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication(scanBasePackages = "org.sample.benchmarks")
public class StringConcatenationOpentracingNoopTracer {

    /*
    Sometimes you way want to initialize some variables that your benchmark code needs,
    but which you do not want to be part of the code your benchmark measures.
    Such variables are called "state" variables.
    */

    @State(Scope.Thread)
    public static class StateVariables {
        StringConcatenationService service1;
        StringConcatenationService service2;

        String a = "Hello ";
        String b = "world";

        //Tell JMH that this method should be called to setup the state object before it
        //is passed to the benchmark method.
        @Setup(Level.Trial)
        public void doSetup() {
            ApplicationContext c = SpringApplication.run(StringConcatenationOpentracingNoopTracer.class);
            service1 = c.getBean(StringConcatenationServiceImpl.class);
        }

        //Tells JMH that this method should be called to clean up ("tear down") the state
        //object after the benchmark has been execute
        @TearDown(Level.Trial)
        public void doTearDown() {

        }

        //https://github.com/opentracing/opentracing-java/tree/master/opentracing-noop
        Tracer tracer = NoopTracerFactory.create();
    }


    @Benchmark
    public void testPlusConcatenation(StateVariables state) {
        String out = state.service1.testPlusConcatenation("a","b");
    }

    @Benchmark
    public void testStringConcatenationStringBuilder(StateVariables state) {
        String out = state.service1.testStringConcatenationStringBuilder("a","b");
    }

    @Benchmark
    public void testStringConcatenationStringBuffer(StateVariables state) {
        String out = state.service1.testStringConcatenationStringBuffer("a","b");
    }
}


