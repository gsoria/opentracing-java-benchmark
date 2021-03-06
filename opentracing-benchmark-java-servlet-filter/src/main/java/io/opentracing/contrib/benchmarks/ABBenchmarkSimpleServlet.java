package io.opentracing.contrib.benchmarks;

import io.opentracing.contrib.benchmarks.util.ResultsFileNameBuilder;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;

import java.io.File;
import java.io.IOException;

public class ABBenchmarkSimpleServlet extends BenchmarkSimpleServletBase {
    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @Warmup(iterations = 1)
    @Measurement(iterations = 1)
    public void testABSimpleRequest(StateVariablesNoInstrumentation state)
            throws Exception {
        String outputFile = ResultsFileNameBuilder.buildResultsFileName("ab-not-instrumented-", ResultFormatType.CSV);
        runABTest(outputFile);
    }

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @Warmup(iterations = 1)
    @Measurement(iterations = 1)
    public void testABSimpleRequestJaegerWithoutMetricFilters(StateVariablesJaegerWithoutMetricFilters state)
            throws Exception {
        String outputFile = ResultsFileNameBuilder.buildResultsFileName("ab-jaeger-", ResultFormatType.CSV);
        runABTest(outputFile);
    }

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @Warmup(iterations = 1)
    @Measurement(iterations = 1)
    public void testABSimpleRequestNooptracerWithoutMetricFilters(StateVariablesNoopTracerWithoutMetricFilters state)
            throws Exception {
        String outputFile = ResultsFileNameBuilder.buildResultsFileName("ab-nooptracer-", ResultFormatType.CSV);
        runABTest(outputFile);
    }

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @Warmup(iterations = 1)
    @Measurement(iterations = 1)
    public void testABSimpleRequestMocktracerWithoutMetricFilters(StateVariablesMockTracerWithoutMetricFilters state)
            throws Exception {
        String outputFile = ResultsFileNameBuilder.buildResultsFileName("ab-mocktracer-", ResultFormatType.CSV);
        runABTest(outputFile);
    }

    @Benchmark
    @BenchmarkMode(Mode.SingleShotTime)
    @Warmup(iterations = 1)
    @Measurement(iterations = 1)
    public void testABSimpleRequestHaystackWithoutMetricFilters(StateVariablesHaystackWithoutMetricFilters state)
            throws Exception {
        String outputFile = ResultsFileNameBuilder.buildResultsFileName("ab-haystack-", ResultFormatType.CSV);
        runABTest(outputFile);
    }

    private void runABTest(String outputFile) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(
                "ab",
                "-g",
                outputFile,
                "-l",
                "-r",
                "-n",
                "50000",
                "-c",
                "10",
                "-k",
                "-H",
                "\"Accept-Encoding: gzip, deflate\"",
                "http://127.0.0.1:9090/jmh-examples-java-servlet-filter");

        pb.directory(new File("."));
        Process p = pb.start();
        try {
            p.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
