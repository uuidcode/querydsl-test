package com.github.uuidcode.querydsl.test.entity;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Id;

import com.github.uuidcode.querydsl.test.util.CoreUtil;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;

public class MetaEntity<T> {
    private String className;
    private String qClassName;
    private String entityName;
    private EntityPathBase<T> entityPathBase;
    private NumberPath<Long> idPath;

    public NumberPath<Long> getIdPath() {
        return this.idPath;
    }

    public MetaEntity setIdPath(NumberPath<Long> idPath) {
        this.idPath = idPath;
        return this;
    }

    public EntityPathBase<T> getEntityPathBase() {
        return this.entityPathBase;
    }

    public MetaEntity setEntityPathBase(EntityPathBase<T> entityPathBase) {
        this.entityPathBase = entityPathBase;
        return this;
    }

    public String getEntityName() {
        return this.entityName;
    }

    public MetaEntity setEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }
    public String getQClassName() {
        return this.qClassName;
    }

    public MetaEntity setQClassName(String qClassName) {
        this.qClassName = qClassName;
        return this;
    }
    public String getClassName() {
        return this.className;
    }

    public MetaEntity setClassName(String className) {
        this.className = className;
        return this;
    }

    @SuppressWarnings("unchecked")
    public static <T> MetaEntity of(Class<T> clazz) {
        try {
            String className = getEntityClassName(clazz);
            String qClassName = getQClassName(className);
            String entityName = getEntityName(className);

            Class entityClass = Class.forName(className);
            Class qClass = Class.forName(qClassName);
            Field idField = getIdField(entityClass);
            Field idFieldOfQClass = qClass.getDeclaredField(idField.getName());
            Field field = qClass.getDeclaredField(entityName);

            EntityPathBase<T> entityPathBase = (EntityPathBase<T>) field.get(qClass);
            NumberPath<Long> idPath = (NumberPath<Long>) idFieldOfQClass.get(entityPathBase);

            return new MetaEntity<T>()
                .setClassName(className)
                .setQClassName(qClassName)
                .setEntityPathBase(entityPathBase)
                .setIdPath(idPath);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public static Field getIdField(Class clazz) {
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

    private static <T> String getEntityClassName(Class<T> clazz) {
        return ((ParameterizedType) clazz.getGenericSuperclass())
            .getActualTypeArguments()[0]
            .getTypeName();
    }

    private static String getEntityName(String className) {
        List<String> list = CoreUtil.split(className, "\\.");
        return CoreUtil.toFirstCharLowerCase(list.get(list.size() - 1));
    }

    private static String getQClassName(String className) {
        return className.replaceAll("entity\\.", "entity.Q");
    }
}
