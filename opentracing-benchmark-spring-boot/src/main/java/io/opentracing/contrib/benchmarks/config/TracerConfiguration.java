package io.opentracing.contrib.benchmarks.config;

import com.expedia.www.haystack.client.dispatchers.Dispatcher;
import com.expedia.www.haystack.client.dispatchers.NoopDispatcher;
import com.expedia.www.haystack.client.metrics.MetricsRegistry;
import com.expedia.www.haystack.client.metrics.NoopMetricsRegistry;
import io.jaegertracing.internal.samplers.ConstSampler;
import io.opentracing.Tracer;
import io.opentracing.mock.MockTracer;
import io.opentracing.noop.NoopTracerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class TracerConfiguration {

    @Profile(TracerImplementation.NOOPTRACER)
    @Bean
    public Tracer createNoopTracer(){
        return NoopTracerFactory.create();
    }

    @Profile(TracerImplementation.MOCKTRACER)
    @Bean
    public Tracer createMockTracer(){
        return new MockTracer();
    }

    @Profile(TracerImplementation.JAEGERTRACER)
    @Bean
    public Tracer createJaegerTracer() {
        io.jaegertracing.Configuration.SamplerConfiguration samplerConfig =
                io.jaegertracing.Configuration.SamplerConfiguration.fromEnv()
                .withType(ConstSampler.TYPE)
                .withParam(1);

        io.jaegertracing.Configuration.ReporterConfiguration reporterConfig =
                io.jaegertracing.Configuration.ReporterConfiguration.fromEnv()
                .withLogSpans(false);

        io.jaegertracing.Configuration config =
                new  io.jaegertracing.Configuration(
                "BillingJaegerTracer")
                .withSampler(samplerConfig)
                .withReporter(reporterConfig);

        Tracer tracer =  config.getTracer();
        return tracer;
    }

    @Profile(TracerImplementation.HAYSTACKTRACER)
    @Bean
    public Tracer createHaystackTracer(){
        MetricsRegistry metrics = new NoopMetricsRegistry();
        Dispatcher dispatcher = new NoopDispatcher();
        Tracer tracer =
                new com.expedia.www.haystack.client.Tracer.Builder(metrics,
                        "BillingHaystackTracer", dispatcher).build();
        return tracer;
    }

    //This is necessary to load spring context
    @Profile(TracerImplementation.NO_INSTRUMENTATION)
    @Bean
    public Tracer notInstrumented(){
        return NoopTracerFactory.create();
    }
}
