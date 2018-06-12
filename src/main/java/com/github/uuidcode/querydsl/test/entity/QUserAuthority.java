package com.github.uuidcode.querydsl.test.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserAuthority is a Querydsl query type for UserAuthority
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUserAuthority extends EntityPathBase<UserAuthority> {

    private static final long serialVersionUID = 2006083448L;

    public static final QUserAuthority userAuthority = new QUserAuthority("userAuthority");

    public final StringPath authority = createString("authority");

    public final NumberPath<Long> authorityId = createNumber("authorityId", Long.class);

    public final DateTimePath<java.util.Date> modDatetime = createDateTime("modDatetime", java.util.Date.class);

    public final DateTimePath<java.util.Date> regDatetime = createDateTime("regDatetime", java.util.Date.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QUserAuthority(String variable) {
        super(UserAuthority.class, forVariable(variable));
    }

    public QUserAuthority(Path<? extends UserAuthority> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserAuthority(PathMetadata metadata) {
        super(UserAuthority.class, metadata);
    }

}

