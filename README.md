# jmh-examples

This repo contains some basic benchmark tests using JHM:

- String concatenation (+ / StringBuilder/StringBuffer)
- Array Add Operation (ArrayList/LinkedList)
- Date Comparison (java.time.Instant/java.util.Date)

To generate the jar:

`
mvn clean install
`

And then execute:

`
java -jar target/benchmarks.jar
`

Or if you want to write the results in an output file:

`
java -jar target/benchmarks.jar -rf csv -rff results.csv
`
