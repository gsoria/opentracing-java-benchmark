package com.autentia.training.course.listener;

import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.core.env.AbstractEnvironment;

import javax.annotation.ManagedBean;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

@ManagedBean
public class ExecutorListener implements ServletContextInitializer {

    @Autowired
    private Tracer tracer;

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        String profile =
                System.getProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME);

        if (("noopTracer".equalsIgnoreCase(profile) || "mockTracer".equalsIgnoreCase(profile) ||
                "jaegerTracer".equalsIgnoreCase(profile) || "haystackTracer".equalsIgnoreCase(profile))
                && tracer != null){
            if (!GlobalTracer.isRegistered()) {
                System.out.println("registrando tracer aun no registrado: " + tracer.getClass().getCanonicalName());
                GlobalTracer.register(tracer);
            }
        }
    }
}
