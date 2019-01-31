package org.sample.listeners;

import io.jaegertracing.internal.samplers.ConstSampler;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class TracingServletContextListener
        implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        Tracer tracer = getTracer();
        GlobalTracer.register(tracer);
    }

    public Tracer getTracer(){
        io.jaegertracing.Configuration.SamplerConfiguration samplerConfig =
                io.jaegertracing.Configuration.SamplerConfiguration.fromEnv()
                        .withType(ConstSampler.TYPE)
                        .withParam(1);

        io.jaegertracing.Configuration.ReporterConfiguration reporterConfig =
                io.jaegertracing.Configuration.ReporterConfiguration.fromEnv()
                        .withLogSpans(true);

        io.jaegertracing.Configuration config =
                new  io.jaegertracing.Configuration(
                        "JavaServletFilterJaegerTracer")
                        .withSampler(samplerConfig)
                        .withReporter(reporterConfig);

        Tracer tracer =  config.getTracer();
        return tracer;
    }
}


