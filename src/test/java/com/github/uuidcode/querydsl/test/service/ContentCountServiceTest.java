package com.github.uuidcode.querydsl.test.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.uuidcode.querydsl.test.CoreTest;
import com.github.uuidcode.querydsl.test.entity.ContentCount;

public class ContentCountServiceTest extends CoreTest {
    @Autowired
    private ContentCountService contentCountService;

    @Test
    public void save() {
        this.contentCountService.save(ContentCount.of()
            .setContentType(ContentCount.ContentType.USER)
            .setCountType(ContentCount.CountType.VIEW)
            .setContentId(1L)
            .setCount(2L));
    }
}