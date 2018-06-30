package com.github.uuidcode.querydsl.test.resolver;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.github.uuidcode.querydsl.test.entity.Author;
import com.github.uuidcode.querydsl.test.service.AuthorService;

@Component
public class AuthorQueryResolver implements GraphQLQueryResolver {
    @Autowired
    private AuthorService authorService;

    public List<Author> findAllAuthor() {
        return this.authorService.findAll();
    }
}
