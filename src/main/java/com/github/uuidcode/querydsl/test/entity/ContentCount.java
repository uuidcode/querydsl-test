package com.github.uuidcode.querydsl.test.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import static com.github.uuidcode.querydsl.test.entity.EntityEntry.GENERATOR_NAME;

@Entity
public class ContentCount {
    public enum CountType {
        CLICK, VIEW;
    }

    public enum ContentType {
        USER, BOOK;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = GENERATOR_NAME)
    @GenericGenerator(name = GENERATOR_NAME, strategy = GENERATOR_NAME)
    private Long countId;
    @Enumerated(EnumType.STRING)
    private CountType countType;
    private Long contentId;
    private Long count;
    private Date regDatetime;
    @Transient
    private List<Long> contentIdList;
    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    public ContentType getContentType() {
        return this.contentType;
    }

    public ContentCount setContentType(ContentType contentType) {
        this.contentType = contentType;
        return this;
    }

    public List<Long> getContentIdList() {
        return this.contentIdList;
    }

    public ContentCount setContentIdList(List<Long> contentIdList) {
        this.contentIdList = contentIdList;
        return this;
    }

    public Long getContentId() {
        return this.contentId;
    }

    public ContentCount setContentId(Long contentId) {
        this.contentId = contentId;
        return this;
    }
    public static ContentCount of() {
        return new ContentCount();
    }

    public Date getRegDatetime() {
        return this.regDatetime;
    }

    public ContentCount setRegDatetime(Date regDatetime) {
        this.regDatetime = regDatetime;
        return this;
    }

    public Long getCount() {
        return this.count;
    }

    public ContentCount setCount(Long count) {
        this.count = count;
        return this;
    }

    public CountType getCountType() {
        return this.countType;
    }

    public ContentCount setCountType(CountType countType) {
        this.countType = countType;
        return this;
    }
    public Long getCountId() {
        return this.countId;
    }

    public ContentCount setCountId(Long countId) {
        this.countId = countId;
        return this;
    }
}
