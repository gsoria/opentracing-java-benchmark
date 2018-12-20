package org.sample.benchmarks.service.impl;

import org.sample.benchmarks.service.StringConcatenationService;
import org.springframework.stereotype.Service;

@Service
public class StringConcatenationServiceImpl implements StringConcatenationService {

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
