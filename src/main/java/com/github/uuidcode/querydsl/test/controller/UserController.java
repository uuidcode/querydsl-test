package com.github.uuidcode.querydsl.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.uuidcode.querydsl.test.entity.Payload;
import com.github.uuidcode.querydsl.test.entity.User;
import com.github.uuidcode.querydsl.test.service.UserService;
import com.querydsl.core.types.Predicate;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public Payload list(@QuerydslPredicate(root = User.class) Predicate predicate, Pageable pageable) {
        return this.userService.findAllWithJoin(predicate, pageable);
    }
}
