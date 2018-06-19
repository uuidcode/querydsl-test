package com.github.uuidcode.querydsl.test.service;

import static java.util.stream.Collectors.groupingBy;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.QueryDslJpaRepository;

import com.github.uuidcode.querydsl.test.entity.Book;

public class QuerydslService<T, ID extends Serializable> extends QueryDslJpaRepository<T, ID> {
    public QuerydslService(Class<T> domainClass, EntityManager entityManager) {
        super((JpaEntityInformation<T, ID>) JpaEntityInformationSupport.getEntityInformation(domainClass, entityManager), entityManager);
    }
}
