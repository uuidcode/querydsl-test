package com.github.uuidcode.querydsl.test.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.github.uuidcode.querydsl.test.entity.User;

@Repository
public interface UserDao {
    List<User> list2();
}
