package com.github.uuidcode.querydsl.test.service;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.uuidcode.querydsl.test.entity.Post;
import com.github.uuidcode.querydsl.test.entity.QPost;

@Service
public class PostService extends QuerydslService<Post, Long> {
    @Autowired
    public PostService(EntityManager entityManager) {
        super(Post.class, entityManager);
    }

    public List<Post> findByAuthorId(Long authorId) {
        return this.findAll(QPost.post.authorId.eq(authorId));
    }
}
