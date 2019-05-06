# Latest results

The latest results are located [here](http://jmh.morethan.io/?sources=https://raw.githubusercontent.com/gsoria/opentracing-java-benchmark/master/opentracing-benchmark-java-servlet-filter/results/jmh-2019-04-10-19-44-14.json,https://raw.githubusercontent.com/gsoria/opentracing-java-benchmark/master/opentracing-benchmark-java-servlet-filter/results/jmh-2019-04-10-20-02-27.json,https://raw.githubusercontent.com/gsoria/opentracing-java-benchmark/master/opentracing-benchmark-java-servlet-filter/results/jmh-2019-04-10-20-14-22.json&topBar=Opentracing%20java%20servlet%20filter).
These graphics are constructed based on raw results located in the ``results`` folder.

## Description

These tests use a [servlet example](https://www.ntu.edu.sg/home/ehchua/programming/java/JavaServlets.html) application to process a GET request and return an HTML page with information of the request an a random number assigned.

Using Undertow Deployment API the servlet is deployed, and a new instance off Undertow server is launched in [`BenchmarkSimpleServletBase`](src/main/java/io/opentracing/contrib/benchmarks/BenchmarkSimpleServletBase.java) 

Using system properties, in each tests, the tracer implementation is specified. The tracer is registered in `GlobalTracer` in the moment of on startup using an application listener [`TracingServletContextListener`](src/main/java/io/opentracing/contrib/benchmarks/listeners/TracingServletContextListener.java)

The tests consist of making a simple request using unirest to get the hello world page in a not instrumentation scenario and instrumented with different tracers.

```java
String r = Unirest.get("http://" + HOST + ":" + PORT + "/").asString().getBody();
```

## Dependencies

This project uses this [Opentracing dependency](https://github.com/opentracing-contrib/java-web-servlet-filter):

```xml

    <opentracing.version>0.31.0</opentracing.version>
    <opentracing.java.servlet.filter.version>0.2.2</opentracing.java.servlet.filter.version>
    <jaeger.version>0.31.0</jaeger.version>
    <haystack.version>0.2.5</haystack.version>

    <dependency>
        <groupId>io.opentracing.contrib</groupId>
        <artifactId>opentracing-web-servlet-filter</artifactId>
        <version>${opentracing.java.servlet.filter.version}</version>
    </dependency>
```

And for the different tracer implementations these dependencies:

```xml
    <dependency>
        <groupId>io.opentracing</groupId>
        <artifactId>opentracing-noop</artifactId>
        <version>${opentracing.version}</version>
    </dependency>
    <dependency>
        <groupId>io.opentracing</groupId>
        <artifactId>opentracing-mock</artifactId>
        <version>${opentracing.version}</version>
    </dependency>
    <dependency>
        <groupId>io.jaegertracing</groupId>
        <artifactId>jaeger-client</artifactId>
        <version>${jaeger.version}</version>
    </dependency>
    <dependency>
        <groupId>com.expedia.www</groupId>
        <artifactId>haystack-client-core</artifactId>
        <version>${haystack.version}</version>
    </dependency>
```

## SampleTime metrics

- X axis: represents each execution result.
- Y axis: represents how long time it takes for the benchmark method to execute.

![BenchmarkSimpleServletSampleTime-3](results-imgs/BenchmarkSimpleServletSampleTime.3.png)

![BenchmarkSimpleServletSampleTime-4](results-imgs/BenchmarkSimpleServletSampleTime.4.png)

## Throughput metrics

- X axis: represents each execution result.
- Y axis: represents of number of operations per second  (the number of times per second the benchmark method could be executed).

![BenchmarkSimpleServletThroughput-3](results-imgs/BenchmarkSimpleServletThroughput.3.png)

![BenchmarkSimpleServletThroughput-4](results-imgs/BenchmarkSimpleServletThroughput.4.png)

## Previous results
The previous results are located [here](results-md/previous.md).

## Environment
The tests were executed in a personal notebook with these characteristics:

- Model Name:	MacBook Pro
- Processor Name:	Intel Core i5
- Processor Speed:	2.6 GHz
- Number of Processors:	1
- Total Number of Cores:	2
- L2 Cache (per Core):	256 KB
- L3 Cache:	3 MB
- Memory:	8 GB
