package io.opentracing.contrib.benchmarks;

import com.expedia.www.haystack.client.dispatchers.Dispatcher;
import com.expedia.www.haystack.client.dispatchers.NoopDispatcher;
import com.expedia.www.haystack.client.metrics.MetricsRegistry;
import com.expedia.www.haystack.client.metrics.NoopMetricsRegistry;
import io.jaegertracing.Configuration;
import io.jaegertracing.internal.samplers.ConstSampler;
import io.opentracing.Tracer;
import io.opentracing.mock.MockTracer;
import io.opentracing.noop.NoopTracerFactory;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

public class BenchmarkStringConcatenation {

    /*
    Sometimes you way want to initialize some variables that your benchmark code needs,
    but which you do not want to be part of the code your benchmark measures.
    Such variables are called "state" variables.
    */

    @State(Scope.Thread)
    public static class StateVariables {
        String a = "Hello ";
        String b = "world";
        int i = 0; // the iteration number

        //Tracers
        //https://github.com/opentracing/opentracing-java/tree/master/opentracing-noop
        Tracer noopTracer = NoopTracerFactory.create();

        //https://github.com/opentracing/opentracing-java/tree/master/opentracing-mock
        MockTracer mockTracer = new MockTracer();

        Tracer jaegerTracer = createJaegerTracer();

        Tracer haystackTracer = createHaystackTracer();

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

        private Tracer createHaystackTracer() {
            MetricsRegistry metrics = new NoopMetricsRegistry();
            Dispatcher dispatcher = new NoopDispatcher();
            Tracer tracer = new com.expedia.www.haystack.client.Tracer.Builder(metrics, "BenchmarkStringConcatenation", dispatcher).build();
            return tracer;
        }
    }

    public String doNoInstrumentation(StateVariables state) {
        return getLogMessage(state);
    }

    public void doNoopTracer(StateVariables state) {
        try (io.opentracing.Scope scope = state.noopTracer.buildSpan("testStringConcatenationStringBuilder").startActive(true)) {
            scope.span().setTag("tracer", "noop").log(getLogMessage(state));
        }
    }

    public void doMockTracer(StateVariables state) {
        try (io.opentracing.Scope scope = state.mockTracer.buildSpan("testStringConcatenationStringBuilder").startActive(true)) {
            scope.span().setTag("tracer", "mock").log(getLogMessage(state));
        }
    }

    public void doJaegerTracer(StateVariables state) {
        try (io.opentracing.Scope scope = state.jaegerTracer.buildSpan("testStringConcatenationStringBuilder").startActive(true)) {
            scope.span().setTag("tracer", "jaeger").log(getLogMessage(state));
        }
    }

    public void doHaystackTracer(StateVariables state){
        try (io.opentracing.Scope scope = state.haystackTracer.buildSpan("testStringConcatenationStringBuilder").startActive(true)) {
            scope.span().setTag("tracer", "haystack").log(getLogMessage(state));
        }
    }

    public String getLogMessage(StateVariables state) {
        state.i++;
        return new StringBuilder().append(state.a).append(state.b).append(state.i).toString();
    }
}


