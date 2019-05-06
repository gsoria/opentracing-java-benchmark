package io.opentracing.contrib.benchmarks.config;

import com.expedia.www.haystack.client.dispatchers.Dispatcher;
import com.expedia.www.haystack.client.dispatchers.NoopDispatcher;
import com.expedia.www.haystack.client.metrics.MetricsRegistry;
import com.expedia.www.haystack.client.metrics.NoopMetricsRegistry;
import io.jaegertracing.Configuration;
import io.opentracing.Tracer;
import io.opentracing.mock.MockTracer;
import io.opentracing.noop.NoopTracerFactory;

public class TracerConfiguration {

    public static Tracer getTracer() {
        String tracerImplementation = System.getProperty("tracerImplementation");

        TracerImplementation implementation = TracerImplementation.valueOf(tracerImplementation);

        Tracer tracer = getNoopTracer();
        switch (implementation) {
            case JAEGERTRACER:
                tracer = getJaegerTracer();
                break;
            case HAYSTACKTRACER:
                tracer = getHaystackTracer();
                break;
            case MOCKTRACER:
                tracer = getMockTracer();
                break;
            case NOOPTRACER:
                tracer = getNoopTracer();
        }
        return tracer;
    }

    private static Tracer getNoopTracer() {
        return NoopTracerFactory.create();
    }

    private static Tracer getMockTracer() {
        return new MockTracer();
    }

    private static Tracer getHaystackTracer() {
        MetricsRegistry metrics = new NoopMetricsRegistry();
        Dispatcher dispatcher = new NoopDispatcher();

        Tracer tracer = new com.expedia.www.haystack.client.Tracer.Builder(
                metrics, "JavaServletFilter", dispatcher).build();
        return tracer;
    }

    private static Tracer getJaegerTracer() {
        Configuration.SamplerConfiguration samplerConfig =
                Configuration.SamplerConfiguration
                        .fromEnv().withType("const")
                        .withParam(Integer.valueOf(1));

        Configuration.ReporterConfiguration reporterConfig =
                Configuration.ReporterConfiguration
                        .fromEnv()
                        .withLogSpans(Boolean.valueOf(true));

        Configuration config = new Configuration("JavaServletFilterJaegerTracer")
                .withSampler(samplerConfig).withReporter(reporterConfig);

        Tracer tracer = config.getTracer();
        return tracer;
    }
}
