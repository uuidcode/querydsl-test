package com.github.uuidcode.querydsl.test.resolver;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.github.uuidcode.querydsl.test.entity.Author;
import com.github.uuidcode.querydsl.test.entity.Post;
import com.github.uuidcode.querydsl.test.service.AuthorService;
import com.github.uuidcode.querydsl.test.service.PostService;

@Component
public class AuthorQueryResolver implements GraphQLQueryResolver {
    @Autowired
    private AuthorService authorService;

    @Autowired
    private PostService postService;

    public List<Author> findAllAuthor() {
        List<Author> authorList = this.authorService.findAll();
        authorList.forEach(author -> {
            List<Post> postList = this.postService.findByAuthorId(author.getId());
            author.setPostList(postList);
        });
        return authorList;
    }
}
