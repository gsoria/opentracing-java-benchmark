package org.sample.listeners;

import io.opentracing.Tracer;
import io.opentracing.contrib.web.servlet.filter.TracingFilter;
import io.opentracing.noop.NoopTracerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class TracingServletContextListener
        implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext ctx = servletContextEvent.getServletContext();
        Tracer tracer = getTracer();
        TracingFilter filter = new TracingFilter(tracer);
        ctx.addFilter("tracingFilter", filter);
    }

    public Tracer getTracer(){
        return NoopTracerFactory.create();
    }
}


