package io.opentracing.contrib.benchmarks.main;

import io.opentracing.contrib.benchmarks.util.ResultsFileNameBuilder;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class Main {
    public static void main(String[] args) {

        ResultFormatType resultsFileOutputType = ResultFormatType.JSON;
        String resultFilePrefix = "jmh-";

        Options opt = new OptionsBuilder()
                .include(".*Benchmark")
                .warmupIterations(5)
                .measurementIterations(5)
                .forks(1)
                .threads(1)
                .resultFormat(resultsFileOutputType)
                .result(ResultsFileNameBuilder.buildResultsFileName(resultFilePrefix,
                        resultsFileOutputType))
                .jvmArgs("-server", "-Xms2048m", "-Xmx2048m")
                .build();
        try {
            new Runner(opt).run();
        } catch (RunnerException e) {
            e.printStackTrace();
        }
    }
}