package com.github.uuidcode.querydsl.test.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.uuidcode.querydsl.test.entity.QUser;
import com.github.uuidcode.querydsl.test.entity.User;

@Service
public class UserService extends EntityService<User> {
    @Autowired
    private UserAuthorityService userAuthorityService;

    public List<User> list() {
        return this.createQuery()
        .select(QUser.user)
        .from(QUser.user)
        .fetch();
    }
}
