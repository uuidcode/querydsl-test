package com.github.uuidcode.querydsl.test.service;

import static java.util.stream.Collectors.groupingBy;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.github.uuidcode.querydsl.test.entity.MetaEntity;
import com.github.uuidcode.querydsl.test.util.CoreUtil;
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

    public void remove(T t) {
        t = this.entityManager.merge(t);
        this.entityManager.remove(t);
    }

    public void remove(Long id) {
        if (id == null) {
            return;
        }

        T t = this.get(id);
        this.remove(t);
    }

    public T update(T t) {
        if (t == null) {
            return null;
        }

        return this.entityManager.merge(t);
    }

    public <PARENT> void join(List<PARENT> list) {
        if (list == null) {
            return;
        }

        if (list.size() == 0) {
            return;
        }

        Class<?> parentClass = list.get(0).getClass();
        Field parentIdField = MetaEntity.getIdField(parentClass);
        parentIdField.setAccessible(true);

        List<Long> idList = list.stream()
            .map(object -> CoreUtil.getId(object))
            .collect(Collectors.toList());

        String parentIdName = parentIdField.getName();

        if (logger.isDebugEnabled()) {
            logger.debug(">>> join parentIdName: {}", CoreUtil.toJson(parentIdName));
        }

        MetaEntity<T> childMetaEntity = MetaEntity.of(this.getClass());
        EntityPathBase<T> qObject = childMetaEntity.getEntityPathBase();
        Class childEntityClass = childMetaEntity.getEntityClass();

        try {
            NumberPath<Long> foreignKeyPath = CoreUtil.getIdPath(qObject, parentIdName);

            List<T> childList = this.createQuery()
                .select(qObject)
                .from(qObject)
                .where(foreignKeyPath.in(idList))
                .fetch();

            Map<Long, List<T>> map = childList.stream()
                .collect(groupingBy(child -> CoreUtil.getId(child, parentIdName)));

            list.forEach(parent -> CoreUtil.invokeSetListMethod(map, parent, childEntityClass));
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
