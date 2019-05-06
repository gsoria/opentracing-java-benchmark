# Latest results

The latest results are located [here](http://jmh.morethan.io/?sources=https://raw.githubusercontent.com/gsoria/opentracing-java-benchmark/master/opentracing-benchmark-spring-cloud/results/jmh-2019-04-07-14-56-15.json,https://raw.githubusercontent.com/gsoria/opentracing-java-benchmark/master/opentracing-benchmark-spring-cloud/results/jmh-2019-04-07-15-18-35.json,https://raw.githubusercontent.com/gsoria/opentracing-java-benchmark/master/opentracing-benchmark-spring-cloud/results/jmh-2019-04-07-15-40-51.json&topBar=Opentracing%20spring%20cloud).
These graphics are constructed based on raw results located in the ``results`` folder.

## Description

These tests use the [petclinic](https://github.com/spring-projects/spring-petclinic) sample spring-based application. The application is 
initialized for each test iteration in the [`BenchmarkPetclinicBase`](src/main/java/io/opentracing/contrib/benchmarks/BenchmarkPetclinicBase.java), 
and using profiles, the right tracer is injected in [`TracerConfiguration`](src/main/java/io/opentracing/contrib/benchmarks/config/TracerConfiguration.java). 
   
The different tests measure the process of finding a pet owner by id in a not instrumentation scenario and instrumented with different tracers. 
The tests are performing the operation calling to the Spring services directly.

## Dependencies

This project uses this [Opentracing dependency](https://github.com/opentracing-contrib/java-spring-cloud):

```xml
    <opentracing.spring.cloud.version>0.2.2</opentracing.spring.cloud.version>
    <opentracing.version>0.31.0</opentracing.version>
    <jaeger.version>0.31.0</jaeger.version>
    <haystack.version>0.2.5</haystack.version>
    
    <!-- Opentracing-->
    <dependency>
        <groupId>io.opentracing.contrib</groupId>
        <artifactId>opentracing-spring-cloud-starter</artifactId>
        <version>${opentracing.spring.cloud.version}</version>
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

![BenchmarkPetclinicSampleTime-3](results-imgs/BenchmarkPetclinicSampleTime.3.png)

![BenchmarkPetclinicSampleTime-4](results-imgs/BenchmarkPetclinicSampleTime.4.png)

## Throughput metrics

- X axis: represents each execution result.
- Y axis: represents of number of operations per second  (the number of times per second the benchmark method could be executed).

![BenchmarkPetclinicThroughput-3](results-imgs/BenchmarkPetclinicThroughput.3.png)

![BenchmarkPetclinicThroughput-4](results-imgs/BenchmarkPetclinicThroughput.4.png)

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
