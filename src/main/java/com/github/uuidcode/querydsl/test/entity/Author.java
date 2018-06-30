package com.github.uuidcode.querydsl.test.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import static com.github.uuidcode.querydsl.test.entity.EntityEntry.GENERATOR_NAME;

@Entity
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = GENERATOR_NAME)
    @GenericGenerator(name = GENERATOR_NAME, strategy = GENERATOR_NAME)
    private Long id;
    private String name;
    private String thumbnail;
    @Transient
    private List<Post> postList;

    public static Author of() {
        return new Author();
    }

    public List<Post> getPostList() {
        return this.postList;
    }

    public Author setPostList(List<Post> postList) {
        this.postList = postList;
        return this;
    }
    public String getThumbnail() {
        return this.thumbnail;
    }

    public Author setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }
    public String getName() {
        return this.name;
    }

    public Author setName(String name) {
        this.name = name;
        return this;
    }
    public Long getId() {
        return this.id;
    }

    public Author setId(Long id) {
        this.id = id;
        return this;
    }
}
