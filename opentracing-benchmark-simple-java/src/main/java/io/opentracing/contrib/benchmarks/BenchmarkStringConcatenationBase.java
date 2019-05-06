package io.opentracing.contrib.benchmarks;

import com.expedia.www.haystack.client.dispatchers.Dispatcher;
import com.expedia.www.haystack.client.dispatchers.NoopDispatcher;
import com.expedia.www.haystack.client.metrics.MetricsRegistry;
import com.expedia.www.haystack.client.metrics.NoopMetricsRegistry;
import io.jaegertracing.Configuration;
import io.jaegertracing.internal.samplers.ConstSampler;
import io.opentracing.Tracer;
import io.opentracing.contrib.benchmarks.config.TracerConfiguration;
import io.opentracing.contrib.benchmarks.config.TracerImplementation;
import io.opentracing.mock.MockTracer;
import io.opentracing.noop.NoopTracerFactory;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

public class BenchmarkStringConcatenationBase {

    public static final String TRACER = "tracer";

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
        Tracer noopTracer = TracerConfiguration.getTracer(TracerImplementation.NOOPTRACER);

        //https://github.com/opentracing/opentracing-java/tree/master/opentracing-mock
        MockTracer mockTracer = (MockTracer) TracerConfiguration.getTracer(TracerImplementation.MOCKTRACER);

        //https://github.com/jaegertracing/jaeger-client-java/tree/master/jaeger-client
        Tracer jaegerTracer = TracerConfiguration.getTracer(TracerImplementation.JAEGERTRACER);

        //https://github.com/ExpediaDotCom/haystack-client-java/tree/master/core
        Tracer haystackTracer = TracerConfiguration.getTracer(TracerImplementation.HAYSTACKTRACER);

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
            Tracer tracer = new com.expedia.www.haystack.client.Tracer.Builder(metrics, "BenchmarkStringConcatenationBase", dispatcher).build();
            return tracer;
        }
    }

    public String doNoInstrumentation(StateVariables state) {
        return getLogMessage(state);
    }

    public String doNoopTracer(StateVariables state) {
        try (io.opentracing.Scope scope = state.noopTracer.buildSpan("testStringConcatenationStringBuilder").startActive(true)) {
            String message = getLogMessage(state);
            scope.span().setTag(TRACER, TracerImplementation.NOOPTRACER).log(message);
            return message;
        }
    }

    public String doMockTracer(StateVariables state) {
        try (io.opentracing.Scope scope = state.mockTracer.buildSpan("testStringConcatenationStringBuilder").startActive(true)) {
            String message = getLogMessage(state);
            scope.span().setTag(TRACER, TracerImplementation.MOCKTRACER).log(message);
            return message;
        }
    }

    public String doJaegerTracer(StateVariables state) {
        try (io.opentracing.Scope scope = state.jaegerTracer.buildSpan("testStringConcatenationStringBuilder").startActive(true)) {
            String message = getLogMessage(state);
            scope.span().setTag(TRACER, TracerImplementation.JAEGERTRACER).log(message);
            return message;
        }
    }

    public String doHaystackTracer(StateVariables state){
        try (io.opentracing.Scope scope = state.haystackTracer.buildSpan("testStringConcatenationStringBuilder").startActive(true)) {
            String message = getLogMessage(state);
            scope.span().setTag(TRACER, TracerImplementation.HAYSTACKTRACER).log(message);
            return message;
        }
    }

    public String getLogMessage(StateVariables state) {
        state.i++;
        return new StringBuilder().append(state.a).append(state.b).append(state.i).toString();
    }
}


