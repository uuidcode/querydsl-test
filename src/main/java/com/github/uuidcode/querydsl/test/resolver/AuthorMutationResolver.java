package com.github.uuidcode.querydsl.test.resolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.github.uuidcode.querydsl.test.entity.Author;
import com.github.uuidcode.querydsl.test.service.AuthorService;

@Component
public class AuthorMutationResolver implements GraphQLMutationResolver {
    @Autowired
    private AuthorService authorService;

    public Author writeAuthor(String name, String thumbnail) {
        Author author = Author.of()
            .setName(name)
            .setThumbnail(thumbnail);

        return this.authorService.save(author);
    }
}
