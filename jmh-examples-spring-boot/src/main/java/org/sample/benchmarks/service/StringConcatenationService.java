package org.sample.benchmarks.service;

public interface StringConcatenationService {
    String testPlusConcatenation(String a, String b);
    String testStringConcatenationStringBuilder(String a, String b);
    String testStringConcatenationStringBuffer(String a, String b);
}
