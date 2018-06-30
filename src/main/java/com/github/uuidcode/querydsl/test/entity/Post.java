package com.github.uuidcode.querydsl.test.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import static com.github.uuidcode.querydsl.test.entity.EntityEntry.GENERATOR_NAME;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = GENERATOR_NAME)
    @GenericGenerator(name = GENERATOR_NAME, strategy = GENERATOR_NAME)
    private Long id;
    private String title;
    private String text;
    private String category;
    private Long authorId;
    @Transient
    private Author author;

    public Author getAuthor() {
        return this.author;
    }

    public Post setAuthor(Author author) {
        this.author = author;
        return this;
    }

    public static Post of() {
        return new Post();
    }

    public Long getAuthorId() {
        return this.authorId;
    }

    public Post setAuthorId(Long authorId) {
        this.authorId = authorId;
        return this;
    }
    public String getCategory() {
        return this.category;
    }

    public Post setCategory(String category) {
        this.category = category;
        return this;
    }
    public String getText() {
        return this.text;
    }

    public Post setText(String text) {
        this.text = text;
        return this;
    }
    public String getTitle() {
        return this.title;
    }

    public Post setTitle(String title) {
        this.title = title;
        return this;
    }
    public Long getId() {
        return this.id;
    }

    public Post setId(Long id) {
        this.id = id;
        return this;
    }
}
