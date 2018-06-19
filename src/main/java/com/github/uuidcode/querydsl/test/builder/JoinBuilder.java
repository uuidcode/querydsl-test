package com.github.uuidcode.querydsl.test.builder;

import static java.util.stream.Collectors.groupingBy;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class JoinBuilder<P, C> {
    private List<P> parentList;
    private List<C> childList;
    private Function<P, Long> parentIdFunction;
    private Function<C, Long> childIdFunction;
    private BiFunction<P, List<C>, P> setterFunction;

    public static JoinBuilder of() {
        return new JoinBuilder();
    }

    public void build() {
        Map<Long, List<C>> map = this.childList.stream()
            .collect(groupingBy(this.childIdFunction));

        parentList.forEach(user -> {
            List<C> list = map.get(this.parentIdFunction);
            this.setterFunction.apply(user, list);
        });
    }
    public BiFunction<P, List<C>, P> getSetterFunction() {
        return this.setterFunction;
    }

    public JoinBuilder setSetterFunction(BiFunction<P, List<C>, P> setterFunction) {
        this.setterFunction = setterFunction;
        return this;
    }

    public Function<C, Long> getChildIdFunction() {
        return this.childIdFunction;
    }

    public JoinBuilder setChildIdFunction(Function<C, Long> childIdFunction) {
        this.childIdFunction = childIdFunction;
        return this;
    }

    public Function<P, Long> getParentIdFunction() {
        return this.parentIdFunction;
    }

    public JoinBuilder setParentIdFunction(Function<P, Long> parentIdFunction) {
        this.parentIdFunction = parentIdFunction;
        return this;
    }

    public List<C> getChildList() {
        return this.childList;
    }

    public JoinBuilder setChildList(List<C> childList) {
        this.childList = childList;
        return this;
    }

    public List<P> getParentList() {
        return this.parentList;
    }

    public JoinBuilder setParentList(List<P> parentList) {
        this.parentList = parentList;
        return this;
    }
}
