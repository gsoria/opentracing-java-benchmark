package com.autentia.training.course.listener;

import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletContextInitializer;

import javax.annotation.ManagedBean;
import javax.servlet.ServletContext;

@ManagedBean
public class ExecutorListener implements ServletContextInitializer {

    @Autowired(required = false)
    private Tracer tracer;

    @Override
    public void onStartup(ServletContext servletContext) {
        if (tracer != null && !GlobalTracer.isRegistered()) {
            GlobalTracer.register(tracer);
        }
    }
}
