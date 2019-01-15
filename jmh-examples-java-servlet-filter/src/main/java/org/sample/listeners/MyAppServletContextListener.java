package org.sample.listeners;

import io.opentracing.Tracer;
import io.opentracing.contrib.web.servlet.filter.TracingFilter;
import io.opentracing.noop.NoopTracerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class MyAppServletContextListener
        implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("ServletContextListener destroyed");
    }

    //Run this before web application is started
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("ServletContextListener started");
        ServletContext ctx = servletContextEvent.getServletContext();
        Tracer tracer = getTracer();
        TracingFilter filter = new TracingFilter(tracer);
        ctx.addFilter("tracingFilter", filter);

        System.out.println("tracingFiler added + "  + tracer.getClass().getName());
    }

    public Tracer getTracer(){
        return NoopTracerFactory.create();
    }
}


