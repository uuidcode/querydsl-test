package com.github.uuidcode.querydsl.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.uuidcode.querydsl.test.entity.Payload;
import com.github.uuidcode.querydsl.test.entity.User;
import com.github.uuidcode.querydsl.test.service.UserService2;
import com.querydsl.core.types.Predicate;

@Controller
public class UserController {
    @Autowired
    private UserService2 userService2;

    @RequestMapping("/user")
    @ResponseBody
    public Payload list(@QuerydslPredicate(root = User.class) Predicate predicate, Pageable pageable) {
        return this.userService2.findAllWithJoin(predicate, pageable);
    }
}
