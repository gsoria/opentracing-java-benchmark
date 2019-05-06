package io.opentracing.contrib.benchmarks;

import io.opentracing.contrib.benchmarks.billing.BillingApplication;
import io.opentracing.contrib.benchmarks.config.TracerImplementation;
import org.junit.After;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.AbstractEnvironment;

import static org.junit.Assert.assertNotNull;

public class BenchmarkBillingTests {

    private ConfigurableApplicationContext context;

    @Test
    public void loadSpringContextWithoutInstrumentation() {
        loadSpringContext(null);
        BillingApplication billing = context.getBean(BillingApplication.class);
        assertNotNull(billing);
    }

    @Test
    public void loadSpringContextWithNoopTracer() {
        loadSpringContext(TracerImplementation.NOOPTRACER);
        BillingApplication billing = context.getBean(BillingApplication.class);
        assertNotNull(billing);
    }

    @Test
    public void loadSpringContextWithMockTracer() {
        loadSpringContext(TracerImplementation.MOCKTRACER);
        BillingApplication billing = context.getBean(BillingApplication.class);
        assertNotNull(billing);
    }

    @Test
    public void loadSpringContextWithJaegerTracer() {
        loadSpringContext(TracerImplementation.JAEGERTRACER);
        BillingApplication billing = context.getBean(BillingApplication.class);
        assertNotNull(billing);
    }

    @Test
    public void loadSpringContextWithHaystackTracer() {
        loadSpringContext(TracerImplementation.HAYSTACKTRACER);
        BillingApplication billing = context.getBean(BillingApplication.class);
        assertNotNull(billing);
    }

    @After
    public void closeContext() {
        context.close();
    }

    private void loadSpringContext(String profile) {
        if(profile != null){
            System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, profile);
        }
        context = SpringApplication.run(BillingApplication.class);
    }
}
