package org.sample.benchmarks;

import org.openjdk.jmh.annotations.*;
import org.sample.benchmarks.service.StringConcatenationService;
import org.sample.benchmarks.service.impl.StringConcatenationMockTracerServiceImpl;
import org.sample.benchmarks.service.impl.StringConcatenationNoopTracerServiceImpl;
import org.sample.benchmarks.service.impl.StringConcatenationServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication(scanBasePackages = "org.sample.benchmarks")
public class BenchmarkStringConcatenation {

    /*
    Sometimes you way want to initialize some variables that your benchmark code needs,
    but which you do not want to be part of the code your benchmark measures.
    Such variables are called "state" variables.
    */

    @State(Scope.Thread)
    public static class StateVariables {
        StringConcatenationService service;
        StringConcatenationService serviceNoopTracer;
        StringConcatenationService serviceMockTracer;

        String a = "Hello ";
        String b = "world";

        //Tell JMH that this method should be called to setup the state object before it
        //is passed to the benchmark method.
        @Setup(Level.Trial)
        public void doSetup() {
            ApplicationContext c = SpringApplication.run(BenchmarkStringConcatenation.class);
            service = c.getBean(StringConcatenationServiceImpl.class);
            serviceMockTracer = c.getBean(StringConcatenationMockTracerServiceImpl.class);
            serviceNoopTracer = c.getBean(StringConcatenationNoopTracerServiceImpl.class);
        }

        //Tells JMH that this method should be called to clean up ("tear down") the state
        //object after the benchmark has been execute
        @TearDown(Level.Iteration)
        public void doTearDown() {
            StringConcatenationMockTracerServiceImpl service = (StringConcatenationMockTracerServiceImpl)serviceMockTracer;
            service.resetTracer();
        }
    }


    @Benchmark
    public void testPlusConcatenation(StateVariables state) {
        String out = state.service.testPlusConcatenation("a","b");
    }

    @Benchmark
    public void testStringConcatenationStringBuilder(StateVariables state) {
        String out = state.service.testStringConcatenationStringBuilder("a","b");
    }

    @Benchmark
    public void testStringConcatenationStringBuffer(StateVariables state) {
        String out = state.service.testStringConcatenationStringBuffer("a","b");
    }

    @Benchmark
    public void testPlusConcatenationNoopTracer(StateVariables state) {
        String out = state.serviceNoopTracer.testPlusConcatenation("a","b");
    }

    @Benchmark
    public void testStringConcatenationStringBuilderNoopTracer(StateVariables state) {
        String out = state.serviceNoopTracer.testStringConcatenationStringBuilder("a","b");
    }

    @Benchmark
    public void testStringConcatenationStringBufferNoopTracer(StateVariables state) {
        String out = state.serviceNoopTracer.testStringConcatenationStringBuffer("a","b");
    }

    @Benchmark
    public void testPlusConcatenationMockTracer(StateVariables state) {
        String out = state.serviceMockTracer.testPlusConcatenation("a","b");
    }

    @Benchmark
    public void testStringConcatenationStringBuilderMockTracer(StateVariables state) {
        String out = state.serviceMockTracer.testStringConcatenationStringBuilder("a","b");
    }

    @Benchmark
    public void testStringConcatenationStringBufferMockTracer(StateVariables state) {
        String out = state.serviceMockTracer.testStringConcatenationStringBuffer("a","b");
    }
}


