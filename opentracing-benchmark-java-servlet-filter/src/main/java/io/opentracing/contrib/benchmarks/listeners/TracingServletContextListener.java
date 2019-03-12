package io.opentracing.contrib.benchmarks.listeners;

import com.expedia.www.haystack.client.dispatchers.Dispatcher;
import com.expedia.www.haystack.client.dispatchers.NoopDispatcher;
import com.expedia.www.haystack.client.metrics.MetricsRegistry;
import com.expedia.www.haystack.client.metrics.NoopMetricsRegistry;
import io.jaegertracing.Configuration;
import io.opentracing.Tracer;
import io.opentracing.contrib.benchmarks.enums.TracerImplementation;
import io.opentracing.mock.MockTracer;
import io.opentracing.noop.NoopTracerFactory;
import io.opentracing.util.GlobalTracer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class TracingServletContextListener implements ServletContextListener {

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        Tracer tracer = getTracer();
        GlobalTracer.register(tracer);
    }

    public Tracer getTracer() {
        String tracerImplementation = System.getProperty("tracerImplementation");

        TracerImplementation implementation = TracerImplementation.valueOf(tracerImplementation);

        Tracer tracer = getNoopTracer();
        switch (implementation) {
            case JAEGER:
                tracer = getJaegerTracer();
                break;
            case HAYSTACK:
                tracer = getHaystackTracer();
                break;
            case MOCK:
                tracer = getMockTracer();
                break;
            case NOOP:
                tracer = getNoopTracer();
        }
        return tracer;
    }

    private Tracer getNoopTracer() {
        return NoopTracerFactory.create();
    }

    private Tracer getMockTracer() {
        return new MockTracer();
    }

    private Tracer getHaystackTracer() {
        MetricsRegistry metrics = new NoopMetricsRegistry();
        Dispatcher dispatcher = new NoopDispatcher();

        Tracer tracer = new com.expedia.www.haystack.client.Tracer.Builder(
                metrics, "JavaServletFilter", dispatcher).build();
        return tracer;
    }

    private Tracer getJaegerTracer() {
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