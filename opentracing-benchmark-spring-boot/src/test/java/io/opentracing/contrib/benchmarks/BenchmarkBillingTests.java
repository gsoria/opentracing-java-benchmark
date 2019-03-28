package io.opentracing.contrib.benchmarks;

import io.opentracing.contrib.billing.config.TracerImplementation;
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
        BenchmarkBilling billing = context.getBean(BenchmarkBilling.class);
        assertNotNull(billing);
    }

    @Test
    public void loadSpringContextWithNoopTracer() {
        loadSpringContext(TracerImplementation.NOOPTRACER);
        BenchmarkBilling billing = context.getBean(BenchmarkBilling.class);
        assertNotNull(billing);
    }

    @Test
    public void loadSpringContextWithMockTracer() {
        loadSpringContext(TracerImplementation.MOCKTRACER);
        BenchmarkBilling billing = context.getBean(BenchmarkBilling.class);
        assertNotNull(billing);
    }

    @Test
    public void loadSpringContextWithJaegerTracer() {
        loadSpringContext(TracerImplementation.JAEGERTRACER);
        BenchmarkBilling billing = context.getBean(BenchmarkBilling.class);
        assertNotNull(billing);
    }

    @Test
    public void loadSpringContextWithHaystackTracer() {
        loadSpringContext(TracerImplementation.HAYSTACKTRACER);
        BenchmarkBilling billing = context.getBean(BenchmarkBilling.class);
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
        context = SpringApplication.run(BenchmarkBilling.class);
    }
}
