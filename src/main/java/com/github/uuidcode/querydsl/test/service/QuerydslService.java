package com.github.uuidcode.querydsl.test.service;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.QuerydslJpaRepository;

public class QuerydslService<T, ID extends Serializable> extends QuerydslJpaRepository<T, ID> {
    public QuerydslService(Class<T> domainClass, EntityManager entityManager) {
        super((JpaEntityInformation<T, ID>) JpaEntityInformationSupport.getEntityInformation(domainClass, entityManager),
            entityManager);
    }
}
