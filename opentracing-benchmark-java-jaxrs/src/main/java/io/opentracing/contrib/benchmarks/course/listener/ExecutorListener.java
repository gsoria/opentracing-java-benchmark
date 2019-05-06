package io.opentracing.contrib.benchmarks.course.listener;

import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.web.servlet.ServletContextInitializer;

import javax.annotation.ManagedBean;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

@ConditionalOnBean(name = "tracer")
@ManagedBean
public class ExecutorListener implements ServletContextInitializer {

    @Autowired
    private Tracer tracer;

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        if (tracer != null){
            if (!GlobalTracer.isRegistered()) {
                GlobalTracer.register(tracer);
            }
        }
    }
}
