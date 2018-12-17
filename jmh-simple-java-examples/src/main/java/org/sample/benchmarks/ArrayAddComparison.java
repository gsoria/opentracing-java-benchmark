package org.sample.benchmarks;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ArrayAddComparison {

    @State(Scope.Thread)
    public static class StateVariables {
        List arrayList = new ArrayList();
        List linkedList = new LinkedList();
    }

    @Benchmark
    public void testArrayListAdd(StateVariables state) {
        state.arrayList.add(new Object());
    }

    @Benchmark
    public void testLinkedListAdd(StateVariables state) {
        state.linkedList.add(new Object());
    }
}