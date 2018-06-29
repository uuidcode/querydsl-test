package com.github.uuidcode.querydsl.test.service;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.uuidcode.querydsl.test.entity.Post;

@Service
public class PostService extends QuerydslService<Post, Long> {
    @Autowired
    public PostService(EntityManager entityManager) {
        super(Post.class, entityManager);
    }
}
