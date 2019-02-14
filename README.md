# JMH benchmark for OpenTracing libraries

This repository contains a set of benchmarks to assess the performance of different OpenTracing components and/or libraries. Some tests might include concrete implementations, such as Jaeger and Haystack, mostly for sanity checking purposes.

List of current tests:

| Test                                            | No Instrumentation | Noop Tracer | Mock Tracer | Jaeger Tracer | Haystack Tracer |
| ----------------------------------------------- | ------------------ | ----------- | ----------- | ------------- | --------------- |
| Throughput, one span, one tag, one log message  |          ✓         |      ✓      |       ✓     |       ✓       |        ✗        |
| Time, one span, one tag, one log message        |          ✓         |      ✓      |       ✓     |       ✓       |        ✗        |

## Running it

```
mvn clean install
java -jar target/benchmarks.jar -rf csv -rff results.csv
```

## Feedback

Performance tests are tricky and we are sure that some things might have done better. We welcome your constructive comments and we appreciate PRs.