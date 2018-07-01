package com.github.uuidcode.querydsl.test.service;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Service;

import com.github.uuidcode.querydsl.test.entity.Post;
import com.github.uuidcode.querydsl.test.entity.QPost;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;

@Service
public class PostService extends QuerydslService<Post, Long> {
    @Autowired
    public PostService(EntityManager entityManager) {
        super(Post.class, entityManager);
    }

    public List<Post> findByAuthorId(Long authorId) {
        return this.findAll(QPost.post.authorId.eq(authorId));
    }

    @GraphQLQuery(name = "getRecentPosts")
    public List<Post> getRecentPosts(@GraphQLArgument(name = "count") int count,
                                     @GraphQLArgument(name = "offset") int offset) {
        Page<Post> page = this.findAll(new QPageRequest(offset, count));
        return page.getContent();
    }

    @GraphQLMutation(name = "writePost")
    public Post writePost(@GraphQLArgument(name = "authorId") Long authorId,
                          @GraphQLArgument(name = "title") String title,
                          @GraphQLArgument(name = "text") String text,
                          @GraphQLArgument(name = "category") String category) {
        Post post = Post.of()
            .setTitle(title)
            .setText(text)
            .setCategory(category)
            .setAuthorId(authorId);

        return this.save(post);
    }
}
