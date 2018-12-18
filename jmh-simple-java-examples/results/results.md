
# 2018-12-17 - Warmup 5

| Benchmark                                                                                             | Mode  | Cnt | Score       | Error         | Units |  
| ----------------------------------------------------------------------------------------------------- |:-----:| ---:|------------:|--------------:|------:|
| org.sample.benchmarks.StringConcatenation.testPlusConcatenation                                       | thrpt | 1 | 5 | 62114821.97 | 1149099.172 | ops/s | 
| org.sample.benchmarks.StringConcatenation.testStringConcatenationStringBuffer                         | thrpt | 1 | 5 | 62769212.97 | 2570151.549 | ops/s | 
| org.sample.benchmarks.StringConcatenation.testStringConcatenationStringBuilder                        | thrpt | 1 | 5 | 61499073.44 | 4485896.245 | ops/s | 
| org.sample.benchmarks.StringConcatenationOpentracingJaegerTracer.testPlusConcatenation                | thrpt | 1 | 5 | 1900726.609 | 66149.14727 | ops/s | 
| org.sample.benchmarks.StringConcatenationOpentracingJaegerTracer.testStringConcatenationStringBuffer  | thrpt | 1 | 5 | 1915714.754 | 52332.15791 | ops/s | 
| org.sample.benchmarks.StringConcatenationOpentracingJaegerTracer.testStringConcatenationStringBuilder | thrpt | 1 | 5 | 1899011.867 | 187092.3438 | ops/s | 
| org.sample.benchmarks.StringConcatenationOpentracingMockTracer.testPlusConcatenation                  | thrpt | 1 | 5 | 301825.503  | 13874.52343 | ops/s | 
| org.sample.benchmarks.StringConcatenationOpentracingMockTracer.testStringConcatenationStringBuffer    | thrpt | 1 | 5 | 300579.9428 | 25579.52972 | ops/s | 
| org.sample.benchmarks.StringConcatenationOpentracingMockTracer.testStringConcatenationStringBuilder   | thrpt | 1 | 5 | 303504.8596 | 23648.26774 | ops/s | 
| org.sample.benchmarks.StringConcatenationOpentracingNoopTracer.testPlusConcatenation                  | thrpt | 1 | 5 | 64917427.39 | 918760.4726 | ops/s | 
| org.sample.benchmarks.StringConcatenationOpentracingNoopTracer.testStringConcatenationStringBuffer    | thrpt | 1 | 5 | 64808971.63 | 2414579.918 | ops/s | 
| org.sample.benchmarks.StringConcatenationOpentracingNoopTracer.testStringConcatenationStringBuilder   | thrpt | 1 | 5 | 64960891.64 | 1067483.275 | ops/s | 


# Warmup 5

| Benchmark                                                           | Mode  | Cnt | Score      | Error         | Units |  
| ------------------------------------------------------------------- |:-----:| ---:|-----------:|--------------:|------:|
| ArrayAddComparison.testLinkedListAdd                                | thrpt | 18 | 5.986       | ± 0.533       | ops/s | 
| DateComparison.testCalendarAfter                                    | thrpt | 25 | 636144322.6 | ± 17514714.28 | ops/s | 
| DateComparison.testInstantAfter                                     | thrpt | 25 | 523297799.1 | ± 15518749.7  | ops/s | 
| StringConcatenation.testPlusConcatenation                           | thrpt | 25 | 59432152.91 | ± 2923103.696 | ops/s | 
| StringConcatenation.testStringConcatenationStringBuffer             | thrpt | 25 | 61414811.54 | ± 1750078.387 | ops/s | 
| StringConcatenation.testStringConcatenationStringBuilder            | thrpt | 25 | 62255519.95 | ± 1852232.402 | ops/s | 
| StringConcatenationOpentracing.testPlusConcatenation                | thrpt | 25 | 61783211.01 | ± 3417401.192 | ops/s | 
| StringConcatenationOpentracing.testStringConcatenationStringBuffer  | thrpt | 25 | 63301116.67 | ± 943808.793  | ops/s | 
| StringConcatenationOpentracing.testStringConcatenationStringBuilder | thrpt | 25 | 64087282.66 | ± 749235.201  | ops/s | 



# Warmup 100

| Benchmark                                                           | Mode  | Cnt | Score       | Error         | Units       |  
| ------------------------------------------------------------------- |:-----:| ---:|------------:|--------------:|------------:|
| StringConcatenation.testPlusConcatenation                           | thrpt | 5   | 61370481.13 | ± 3675306.125 | ops/s       | 
| StringConcatenation.testStringConcatenationStringBuffer             | thrpt | 5   | 54909820.97 | ± 4780041.535 | ops/s       | 
| StringConcatenation.testStringConcatenationStringBuilder            | thrpt | 5   | 61294816.47 | ± 2107543.732 | ops/s       | 
| StringConcatenationOpentracing.testPlusConcatenation                | thrpt | 5   | 62113253.56 | ± 6724575.201 | ops/s       | 
| StringConcatenationOpentracing.testStringConcatenationStringBuffer  | thrpt | 5   | 61375234.75 | ± 5974889.705 | ops/s       | 
| StringConcatenationOpentracing.testStringConcatenationStringBuilder | thrpt | 5   | 62950012.98 | ± 1682243.313 | ops/s       | 

