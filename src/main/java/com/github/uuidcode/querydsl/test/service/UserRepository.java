package com.github.uuidcode.querydsl.test.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.github.uuidcode.querydsl.test.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>,
    QueryDslPredicateExecutor<User> {
}
