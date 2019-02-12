package org.sample.billing.config;

import io.jaegertracing.internal.samplers.ConstSampler;
import io.opentracing.Tracer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TracerConfiguration {

    @Bean(name = "jaegerTracer")
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
}
