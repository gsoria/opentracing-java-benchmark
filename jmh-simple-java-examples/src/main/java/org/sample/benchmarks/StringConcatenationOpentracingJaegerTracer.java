package org.sample.benchmarks;

import io.jaegertracing.Configuration;
import io.opentracing.Tracer;
import io.opentracing.mock.MockTracer;
import io.jaegertracing.internal.samplers.ConstSampler;
import org.openjdk.jmh.annotations.*;

public class StringConcatenationOpentracingJaegerTracer {

    /*
    Sometimes you way want to initialize some variables that your benchmark code needs,
    but which you do not want to be part of the code your benchmark measures.
    Such variables are called "state" variables.
    */

    @State(Scope.Thread)
    public static class StateVariables {
        String a = "Hello ";
        String b = "world";


        Configuration.SamplerConfiguration samplerConfig = Configuration.SamplerConfiguration.fromEnv()
                .withType(ConstSampler.TYPE)
                .withParam(1);

        Configuration.ReporterConfiguration reporterConfig = Configuration.ReporterConfiguration.fromEnv()
                .withLogSpans(true);

        Configuration config = new Configuration("StringConcatenationOpentracingJaegerTracer")
                .withSampler(samplerConfig)
                .withReporter(reporterConfig);

        Tracer tracer =  config.getTracer();

        @Setup(Level.Iteration)
        public void doSetup() {


        }
        //Tells JMH that this method should be called to clean up ("tear down") the state
        //object after the benchmark has been execute
        @TearDown(Level.Iteration)
        public void doTearDown() {
            System.out.println("Do TearDown");
        }
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


