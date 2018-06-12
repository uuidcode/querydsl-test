package com.github.uuidcode.querydsl.test.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QBook is a Querydsl query type for Book
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QBook extends EntityPathBase<Book> {

    private static final long serialVersionUID = -1582142679L;

    public static final QBook book = new QBook("book");

    public final NumberPath<Long> bookId = createNumber("bookId", Long.class);

    public final DateTimePath<java.util.Date> modDatetime = createDateTime("modDatetime", java.util.Date.class);

    public final StringPath name = createString("name");

    public final DateTimePath<java.util.Date> regDatetime = createDateTime("regDatetime", java.util.Date.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QBook(String variable) {
        super(Book.class, forVariable(variable));
    }

    public QBook(Path<? extends Book> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBook(PathMetadata metadata) {
        super(Book.class, metadata);
    }

}

