package com.github.uuidcode.querydsl.test.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QContentCount is a Querydsl query type for ContentCount
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QContentCount extends EntityPathBase<ContentCount> {

    private static final long serialVersionUID = -210239402L;

    public static final QContentCount contentCount = new QContentCount("contentCount");

    public final NumberPath<Long> contentId = createNumber("contentId", Long.class);

    public final EnumPath<ContentCount.ContentType> contentType = createEnum("contentType", ContentCount.ContentType.class);

    public final NumberPath<Long> count = createNumber("count", Long.class);

    public final NumberPath<Long> countId = createNumber("countId", Long.class);

    public final EnumPath<ContentCount.CountType> countType = createEnum("countType", ContentCount.CountType.class);

    public final DateTimePath<java.util.Date> regDatetime = createDateTime("regDatetime", java.util.Date.class);

    public QContentCount(String variable) {
        super(ContentCount.class, forVariable(variable));
    }

    public QContentCount(Path<? extends ContentCount> path) {
        super(path.getType(), path.getMetadata());
    }

    public QContentCount(PathMetadata metadata) {
        super(ContentCount.class, metadata);
    }

}

