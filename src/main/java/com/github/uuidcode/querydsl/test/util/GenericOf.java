package com.github.uuidcode.querydsl.test.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * http://stackoverflow.com/questions/14139437/java-type-generic-as-argument-for-gson
 */
public class GenericOf<X, Y> implements ParameterizedType {
    private final Class<X> container;
    private final Class<Y> wrapped;

    public GenericOf(Class<X> container, Class<Y> wrapped) {
        this.container = container;
        this.wrapped = wrapped;
    }

    public Type[] getActualTypeArguments() {
        return new Type[]{wrapped};
    }

    public Type getRawType() {
        return container;
    }

    public Type getOwnerType() {
        return null;
    }
}
