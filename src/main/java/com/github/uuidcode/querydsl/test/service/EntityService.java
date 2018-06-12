package com.github.uuidcode.querydsl.test.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.github.uuidcode.querydsl.test.entity.MetaEntity;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Transactional
public class EntityService<T> {
    protected static Logger logger = LoggerFactory.getLogger(EntityService.class);

    @PersistenceContext
    private EntityManager entityManager;

    public <T> T get(Long id) {
        if (id == null) {
            return null;
        }

        MetaEntity<T> metaEntity = MetaEntity.of(this.getClass());

        EntityPathBase<T> qObject = metaEntity.getEntityPathBase();
        NumberPath<Long> idPath = metaEntity.getIdPath();

        return (T) this.createQuery()
            .select(qObject)
            .from(qObject)
            .where(idPath.eq(id))
            .fetchOne();
    }

    public JPAQueryFactory createQuery() {
        return new JPAQueryFactory(this.entityManager);
    }

    public T save(T t) {
        this.entityManager.persist(t);
        return t;
    }

    public long remove(Long id) {
        if (id == null) {
            return 0;
        }

        MetaEntity<T> metaEntity = MetaEntity.of(this.getClass());

        return this.createQuery()
            .delete(metaEntity.getEntityPathBase())
            .where(metaEntity.getIdPath().eq(id))
            .execute();
    }

    public T update(T t) {
        if (t == null) {
            return null;
        }

        return this.entityManager.merge(t);
    }
}
