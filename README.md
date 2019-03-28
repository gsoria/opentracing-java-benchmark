# JMH benchmark for OpenTracing libraries

This repository contains a set of benchmarks to assess the performance of different OpenTracing components and/or libraries. Some tests might include concrete implementations, such as Jaeger and Haystack, mostly for sanity checking purposes.

List of current tests:

| Test                                            | No Instrumentation | Noop Tracer | Mock Tracer | Jaeger Tracer | Haystack Tracer |
| ----------------------------------------------- | ------------------ | ----------- | ----------- | ------------- | --------------- |
| Throughput, one span, one tag, one log message  |          ✓         |      ✓      |       ✓     |       ✓       |        ✓        |
| Time, one span, one tag, one log message        |          ✓         |      ✓      |       ✓     |       ✓       |        ✓        |

## Running it
Run the following command (inside on a specific project)

```
mvn clean install
java -jar target/benchmarks.jar
```

## Feedback

Performance tests are tricky and we are sure that some things might have done better. We welcome your constructive comments and we appreciate PRs.

## Results
We use [JMH-visualizer](https://github.com/jzillmann/jmh-visualizer) to present the benchmark results. Thanks [jzillmann](https://github.com/jzillmann) 
for this amazing tool!. 

The results of different scenarios are located in each project:
- [opentracing-benchmark-simple-java](https://github.com/gsoria/opentracing-java-benchmark/tree/master/opentracing-benchmark-simple-java)
- [opentracing-benchmark-spring-boot](https://github.com/gsoria/opentracing-java-benchmark/tree/master/opentracing-benchmark-spring-boot)
- [opentracing-benchmark-java-servlet-filter](https://github.com/gsoria/opentracing-java-benchmark/tree/master/opentracing-benchmark-java-servlet-filter)
- [opentracing-benchmark-spring-cloud](https://github.com/gsoria/opentracing-java-benchmark/tree/master/opentracing-benchmark-spring-cloud)
- [opentracing-benchmark-java-jaxrs](https://github.com/gsoria/opentracing-java-benchmark/tree/master/opentracing-benchmark-java-jaxrs)
- [opentracing-benchmark-java-jdbc](https://github.com/gsoria/opentracing-java-benchmark/tree/master/opentracing-benchmark-java-jdbc)