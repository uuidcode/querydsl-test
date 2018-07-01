package com.github.uuidcode.querydsl.test.service;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.uuidcode.querydsl.test.entity.Author;
import com.github.uuidcode.querydsl.test.entity.Post;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;

@Service
public class AuthorService extends QuerydslService<Author, Long> {
    @Autowired
    private PostService postService;

    @Autowired
    public AuthorService(EntityManager entityManager) {
        super(Author.class, entityManager);
    }

    @GraphQLQuery(name = "findAllAuthor")
    public List<Author> findAllAuthor() {
        List<Author> authorList = this.findAll();

        authorList.forEach(author -> {
            List<Post> postList = this.postService.findByAuthorId(author.getAuthorId());
            author.setPostList(postList);
        });

        return authorList;
    }

    @GraphQLMutation(name = "writeAuthor")
    public Author writeAuthor(@GraphQLArgument(name = "name") String name,
                              @GraphQLArgument(name = "thumbnail") String thumbnail) {
        Author author = Author.of()
            .setName(name)
            .setThumbnail(thumbnail);

        return this.save(author);
    }
}
