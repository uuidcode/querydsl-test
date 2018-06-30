package com.github.uuidcode.querydsl.test.resolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.github.uuidcode.querydsl.test.entity.Post;
import com.github.uuidcode.querydsl.test.service.PostService;

@Component
public class PostMutationResolver implements GraphQLMutationResolver {
    @Autowired
    private PostService postService;

    public Post writePost(Long authorId, String title, String text, String category) {
        Post post = Post.of()
            .setTitle(title)
            .setText(text)
            .setCategory(category)
            .setAuthorId(authorId);

        return this.postService.save(post);
    }
}
