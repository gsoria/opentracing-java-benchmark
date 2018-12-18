package org.sample.benchmarks.service.impl;

import io.opentracing.Tracer;
import org.sample.benchmarks.service.StringConcatenationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class StringConcatenationServiceImpl implements StringConcatenationService {

    @Autowired
    @Qualifier("mockTracer")
    public Tracer tracer;

    @Override
    public String testPlusConcatenation(String a, String b) {
        return a + b;
    }

    @Override
    public String testStringConcatenationStringBuilder(String a, String b) {
        StringBuilder sb = new StringBuilder();
        return sb.append(a).append(b).toString();
    }

    @Override
    public String testStringConcatenationStringBuffer(String a, String b) {
        StringBuffer sb = new StringBuffer();
        return sb.append(a).append(b).toString();
    }
}
