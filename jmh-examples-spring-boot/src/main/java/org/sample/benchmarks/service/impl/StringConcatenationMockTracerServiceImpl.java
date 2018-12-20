package org.sample.benchmarks.service.impl;

import io.opentracing.Tracer;
import io.opentracing.mock.MockTracer;
import org.sample.benchmarks.service.StringConcatenationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class StringConcatenationMockTracerServiceImpl implements StringConcatenationService {

    @Autowired
    @Qualifier("mockTracer")
    public Tracer tracer;

    @Override
    public String testPlusConcatenation(String a, String b) {
        try (io.opentracing.Scope scope = tracer.buildSpan("testPlusConcatenation").startActive(true)) {
            String c = a + b;
            scope.span().log("testPlusConcatenation");
            return c;
        }
    }

    @Override
    public String testStringConcatenationStringBuilder(String a, String b) {
        try (io.opentracing.Scope scope = tracer.buildSpan("testStringConcatenationStringBuilder").startActive(true)) {
            StringBuilder sb = new StringBuilder();
            String c = sb.append(a).append(b).toString();
            scope.span().log("testStringConcatenationStringBuilder");
            return c;
        }
    }

    @Override
    public String testStringConcatenationStringBuffer(String a, String b) {
        try (io.opentracing.Scope scope = tracer.buildSpan("testStringConcatenationStringBuffer").startActive(true)) {
            StringBuffer sb = new StringBuffer();
            String c = sb.append(a).append(b).toString();
            scope.span().log("testStringConcatenationStringBuffer");
            return c;
        }
    }

    public void resetTracer(){
        MockTracer tracer = (MockTracer)this.tracer;
        tracer.reset();
    }
}
