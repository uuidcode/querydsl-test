package com.github.uuidcode.querydsl.test.entity;

import static com.github.uuidcode.querydsl.test.entity.EntityEntry.GENERATOR_NAME;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

@Audited
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = GENERATOR_NAME)
    @GenericGenerator(name = GENERATOR_NAME, strategy = GENERATOR_NAME)
    private Long bookId;
    private String name;
    private Date regDatetime;
    private Date modDatetime;
    private Long userId;

    public Long getUserId() {
        return this.userId;
    }

    public Book setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public static Book of() {
        return new Book();
    }

    public Date getModDatetime() {
        return this.modDatetime;
    }

    public Book setModDatetime(Date modDatetime) {
        this.modDatetime = modDatetime;
        return this;
    }

    public Date getRegDatetime() {
        return this.regDatetime;
    }

    public Book setRegDatetime(Date regDatetime) {
        this.regDatetime = regDatetime;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Book setName(String name) {
        this.name = name;
        return this;
    }

    public Long getBookId() {
        return this.bookId;
    }

    public Book setBookId(Long bookId) {
        this.bookId = bookId;
        return this;
    }
}
