package com.github.uuidcode.querydsl.test.service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.github.uuidcode.querydsl.test.entity.MetaEntity;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;

import static java.util.stream.Collectors.groupingBy;

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
        if (list == null || list.size() == 0) {
            return;
        }

        Class<?> parentClass = list.get(0).getClass();
        Field parentIdField = MetaEntity.getIdField(parentClass);
        parentIdField.setAccessible(true);

        List<Long> idList = list.stream()
            .map(object -> this.getId(object))
            .collect(Collectors.toList());

        String parentIdName = parentIdField.getName();
        MetaEntity<T> childMetaEntity = MetaEntity.of(this.getClass());
        EntityPathBase<T> qObject = childMetaEntity.getEntityPathBase();
        Class childEntityClass = childMetaEntity.getEntityClass();
        NumberPath<Long> foreignKeyPath = this.getIdPath(qObject, parentIdName);

        List<T> childList = this.createQuery()
            .select(qObject)
            .from(qObject)
            .where(foreignKeyPath.in(idList))
            .fetch();

        Map<Long, List<T>> map = childList.stream()
            .collect(groupingBy(child -> this.getId(child, parentIdName)));

        list.forEach(parent -> this.invokeSetListMethod(map, parent, childEntityClass));
    }

    public Long getId(Object object) {
        Field idField = this.getIdField(object.getClass());
        return this.getId(object, idField.getName());
    }

    public Long getId(Object object, String idFieldName) {
        try {
            Field field = object.getClass().getDeclaredField(idFieldName);
            field.setAccessible(true);
            return (Long) field.get(object);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public <T> void invokeSetListMethod(Map<Long, List<T>> map, Object parent, Class childClass) {
        try {
            Class clazz = parent.getClass();
            Field idField = this.getIdField(clazz);
            Long id = (Long) idField.get(parent);
            String setMethodName = "set" + childClass.getSimpleName() + "List";
            Method setMethod = clazz.getDeclaredMethod(setMethodName, List.class);
            setMethod.invoke(parent, map.get(id));
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public Field getIdField(Class clazz) {
        Field[] declaredFields = clazz.getDeclaredFields();
        Field idField = Arrays.stream(declaredFields)
            .filter(field -> field.getAnnotation(Id.class) != null)
            .findFirst()
            .map(field -> {
                field.setAccessible(true);
                return field;
            })
            .orElse(null);

        if (idField == null) {
            throw new RuntimeException("@Id field doesn't exist.");
        }

        return idField;
    }

    public NumberPath<Long> getIdPath(Object qObject, String id) {
        try {
            Field field = qObject.getClass().getDeclaredField(id);
            field.setAccessible(true);
            return (NumberPath<Long>) field.get(qObject);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
