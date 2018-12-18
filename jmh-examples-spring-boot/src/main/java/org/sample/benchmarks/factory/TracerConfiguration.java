package org.sample.benchmarks.factory;

import io.opentracing.Tracer;
import io.opentracing.mock.MockTracer;
import io.opentracing.noop.NoopTracerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TracerConfiguration {

    @Bean(name = "noopTracer" )
    public Tracer createNoopTracer(){
        return NoopTracerFactory.create();
    }

    @Bean(name = "mockTracer")
    public Tracer creatMockTracer(){
        return new MockTracer();
    }
}
