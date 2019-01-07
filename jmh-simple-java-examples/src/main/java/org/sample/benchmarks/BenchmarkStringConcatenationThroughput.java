package org.sample.benchmarks;

import io.jaegertracing.Configuration;
import io.jaegertracing.internal.samplers.ConstSampler;
import io.opentracing.Tracer;
import io.opentracing.mock.MockTracer;
import io.opentracing.noop.NoopTracerFactory;
import org.openjdk.jmh.annotations.*;

public class BenchmarkStringConcatenationThroughput {

    /*
    Sometimes you way want to initialize some variables that your benchmark code needs,
    but which you do not want to be part of the code your benchmark measures.
    Such variables are called "state" variables.
    */

    @State(Scope.Thread)
    public static class StateVariables {
        String a = "Hello ";
        String b = "world";

        //Tracers

        //https://github.com/opentracing/opentracing-java/tree/master/opentracing-noop
        Tracer noopTracer = NoopTracerFactory.create();

        //https://github.com/opentracing/opentracing-java/tree/master/opentracing-mock
        MockTracer mockTracer = new MockTracer();

        Tracer jaegerTracer = createJaegerTracer();

        //Tells JMH that this method should be called to clean up ("tear down") the state
        //object after the benchmark has been execute
        @TearDown(Level.Iteration)
        public void doTearDown() {
            mockTracer.reset();
        }

        private Tracer createJaegerTracer() {
            Configuration.SamplerConfiguration samplerConfig = Configuration.SamplerConfiguration.fromEnv()
                    .withType(ConstSampler.TYPE)
                    .withParam(1);

            Configuration.ReporterConfiguration reporterConfig = Configuration.ReporterConfiguration.fromEnv()
                    .withLogSpans(true);

            Configuration config = new Configuration("StringConcatenationOpentracingJaegerTracer")
                    .withSampler(samplerConfig)
                    .withReporter(reporterConfig);

            return config.getTracer();
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public String testPlusConcatenation(StateVariables state) {
        return state.a + state.b;
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public String testStringConcatenationStringBuilder(StateVariables state) {
        StringBuilder sb = new StringBuilder();
        return sb.append(state.a).append(state.b).toString();
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public String testStringConcatenationStringBuffer(StateVariables state) {
        StringBuffer sb = new StringBuffer();
        return sb.append(state.a).append(state.b).toString();
    }

    //Benchmark Noop Tracer
    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public String testPlusConcatenationNoopTracer(StateVariables state) {
        try (io.opentracing.Scope scope = state.noopTracer.buildSpan("testPlusConcatenation").startActive(true)) {
            String c = state.a + state.b;
            scope.span().log("testPlusConcatenationNoopTracer");
            return c;
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public String testStringConcatenationStringBuilderNoopTracer(StateVariables state) {
        try (io.opentracing.Scope scope = state.noopTracer.buildSpan("testStringConcatenationStringBuilder").startActive(true)) {
            StringBuilder sb = new StringBuilder();
            String c = sb.append(state.a).append(state.b).toString();
            scope.span().log("testStringConcatenationStringBuilderNoopTracer");
            return c;
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public String testStringConcatenationStringBufferNoopTracer(StateVariables state) {
        try (io.opentracing.Scope scope = state.noopTracer.buildSpan("testStringConcatenationStringBuffer").startActive(true)) {
            StringBuffer sb = new StringBuffer();
            String c = sb.append(state.a).append(state.b).toString();
            scope.span().log("testStringConcatenationStringBufferNoopTracer");
            return c;
        }
    }

    //Benchmark Mock Tracer
    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public String testPlusConcatenationMockTracer(StateVariables state) {
        try (io.opentracing.Scope scope = state.mockTracer.buildSpan("testPlusConcatenation").startActive(true)) {
            String c = state.a + state.b;
            scope.span().log("testPlusConcatenationMockTracer");
            return c;
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public String testStringConcatenationStringBuilderMockTracer(StateVariables state) {
        try (io.opentracing.Scope scope = state.mockTracer.buildSpan("testStringConcatenationStringBuilder").startActive(true)) {
            StringBuilder sb = new StringBuilder();
            String c = sb.append(state.a).append(state.b).toString();
            scope.span().log("testStringConcatenationStringBuilderMockTracer");
            return c;
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public String testStringConcatenationStringBufferMockTracer(StateVariables state) {
        try (io.opentracing.Scope scope = state.mockTracer.buildSpan("testStringConcatenationStringBuffer").startActive(true)) {
            StringBuffer sb = new StringBuffer();
            String c = sb.append(state.a).append(state.b).toString();
            scope.span().log("testStringConcatenationStringBufferMockTracer");
            return c;
        }
    }

    //Benchmark Jaeger Tracer
    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public String testPlusConcatenationJaegerTracer(StateVariables state) {
        try (io.opentracing.Scope scope = state.jaegerTracer.buildSpan("testPlusConcatenation").startActive(true)) {
            String c = state.a + state.b;
            scope.span().log("testPlusConcatenationJaegerTracer");
            return c;
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public String testStringConcatenationStringBuilderJaegerTracer(StateVariables state) {
        try (io.opentracing.Scope scope = state.jaegerTracer.buildSpan("testStringConcatenationStringBuilder").startActive(true)) {
            StringBuilder sb = new StringBuilder();
            String c = sb.append(state.a).append(state.b).toString();
            scope.span().log("testStringConcatenationStringBuilderJaegerTracer");
            return c;
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public String testStringConcatenationStringBufferJaegerTracer(StateVariables state) {
        try (io.opentracing.Scope scope = state.jaegerTracer.buildSpan("testStringConcatenationStringBuffer").startActive(true)) {
            StringBuffer sb = new StringBuffer();
            String c = sb.append(state.a).append(state.b).toString();
            scope.span().log("testStringConcatenationStringBufferJaegerTracer");
            return c;
        }
    }
}


