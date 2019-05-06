package io.opentracing.contrib.benchmarks.listeners;

import io.opentracing.Tracer;
import io.opentracing.contrib.benchmarks.config.TracerConfiguration;
import io.opentracing.util.GlobalTracer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class TracingServletContextListener implements ServletContextListener {

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        Tracer tracer = TracerConfiguration.getTracer();
        GlobalTracer.register(tracer);
    }
}