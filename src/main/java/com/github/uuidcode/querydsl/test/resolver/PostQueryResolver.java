package com.github.uuidcode.querydsl.test.resolver;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Component;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.github.uuidcode.querydsl.test.entity.Post;
import com.github.uuidcode.querydsl.test.service.PostService;

@Component
public class PostQueryResolver implements GraphQLQueryResolver {
    @Autowired
    private PostService postService;

    public List<Post> getRecentPosts(int count, int offset) {
        Page<Post> page = this.postService.findAll(new QPageRequest(offset, count));
        return page.getContent();
    }
}
