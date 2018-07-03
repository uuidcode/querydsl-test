package com.github.uuidcode.querydsl.test.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.github.uuidcode.querydsl.test.util.WebContext;

import io.leangen.graphql.annotations.GraphQLIgnore;
import io.leangen.graphql.annotations.GraphQLQuery;

import static com.github.uuidcode.querydsl.test.entity.EntityEntry.GENERATOR_NAME;

@Entity
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = GENERATOR_NAME)
    @GenericGenerator(name = GENERATOR_NAME, strategy = GENERATOR_NAME)
    private Long authorId;
    private String name;
    private String thumbnail;
    @Transient
    private List<Post> postList;
    @Transient
    private String description;

    @GraphQLIgnore
    public String getDescription() {
        return this.description;
    }

    public Author setDescription(String description) {
        this.description = description;
        return this;
    }

    public static Author of() {
        return new Author();
    }

    @GraphQLQuery(name= "postList")
    public List<Post> getPostList() {
        WebContext.get()
            .map(WebContext::getRunnable)
            .ifPresent(Runnable::run);
        return this.postList;
    }

    public Author setPostList(List<Post> postList) {
        this.postList = postList;
        return this;
    }

    @GraphQLQuery(name= "thumbnail")
    public String getThumbnail() {
        return this.thumbnail;
    }

    public Author setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }

    @GraphQLQuery(name= "name")
    public String getName() {
        return this.name;
    }

    public Author setName(String name) {
        this.name = name;
        return this;
    }

    @GraphQLQuery(name = "authorId", description = "창작자 아이디")
    public Long getAuthorId() {
        return this.authorId;
    }

    public Author setAuthorId(Long authorId) {
        this.authorId = authorId;
        return this;
    }
}
