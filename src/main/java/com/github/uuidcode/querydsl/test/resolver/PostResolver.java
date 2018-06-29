package com.github.uuidcode.querydsl.test.resolver;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Service;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.github.uuidcode.querydsl.test.entity.Post;
import com.github.uuidcode.querydsl.test.service.PostService;

@Service
public class PostResolver implements GraphQLResolver<Post> {
    @Autowired
    private PostService postService;

    public List<Post> getRecentPosts(int count, int offset) {
        Page<Post> page = this.postService.findAll(new QPageRequest(offset, count));
        return page.getContent();
    }
}
