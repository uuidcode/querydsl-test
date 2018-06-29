package com.github.uuidcode.querydsl.test.resolver;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.github.uuidcode.querydsl.test.service.PostService;

public class Query implements GraphQLQueryResolver {
    private PostService postService;

    public Query(PostService postService) {
        this.postService = postService;
    }
}
