package org.sample.benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.time.Instant;
import java.util.Date;

public class DateComparison {

    @State(Scope.Thread)
    public static class StateVariables {
        Instant instant = Instant.now();
        Instant instant2 = Instant.now();
        Date date = Date.from(instant);
        Date date2 = Date.from(instant2);
    }

    @Benchmark
    public void testInstantAfter(StateVariables state) {
        state.instant.isAfter(state.instant2);
    }

    @Benchmark
    public void testCalendarAfter(StateVariables state) {
        state.date.after(state.date2);
    }
}