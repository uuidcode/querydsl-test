package com.github.uuidcode.querydsl.test.util;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class IntermediateOperation {
    private Function<Stream<String>, Stream<String>> intermediateFunction;

    public static IntermediateOperation skip(Long index) {
        return new IntermediateOperation()
            .setIntermediateFunction(i -> i.skip(index));
    }

    public static IntermediateOperation map(Function<String, String> mapper) {
        return new IntermediateOperation()
            .setIntermediateFunction(i -> i.map(mapper));
    }

    public static IntermediateOperation filter(Predicate<String> predicate) {
        return new IntermediateOperation().
            setIntermediateFunction(i -> i.filter(predicate));
    }

    public IntermediateOperation setIntermediateFunction(Function<Stream<String>, Stream<String>> intermediateFunction) {
        this.intermediateFunction = intermediateFunction;
        return this;
    }

    public Stream<String> run(Stream<String> stream) {
        return this.intermediateFunction.apply(stream);
    }
}
