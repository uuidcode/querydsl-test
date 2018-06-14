package com.github.uuidcode.querydsl.test.entity;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Id;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.uuidcode.querydsl.test.util.CoreUtil;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;

public class MetaEntity<T> {
    protected static Logger logger = LoggerFactory.getLogger(MetaEntity.class);

    private String className;
    private String qClassName;
    private String entityName;
    private EntityPathBase<T> entityPathBase;
    private NumberPath<Long> idPath;
    private Field idField;
    private Class entityClass;
    private Class qClass;

    public Class getQClass() {
        return this.qClass;
    }

    public MetaEntity setQClass(Class qClass) {
        this.qClass = qClass;
        return this;
    }

    public Class getEntityClass() {
        return this.entityClass;
    }

    public MetaEntity setEntityClass(Class entityClass) {
        this.entityClass = entityClass;
        return this;
    }

    public Field getIdField() {
        return this.idField;
    }

    public MetaEntity setIdField(Field idField) {
        this.idField = idField;
        return this;
    }
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

            if (logger.isDebugEnabled()) {
                logger.debug(">>> of className: {}", CoreUtil.toJson(className));
                logger.debug(">>> of entityName: {}", CoreUtil.toJson(entityName));
                logger.debug(">>> of qClassName: {}", CoreUtil.toJson(qClassName));
            }

            Class entityClass = Class.forName(className);
            Class qClass = Class.forName(qClassName);
            Field idField = getIdField(entityClass);
            idField.setAccessible(true);
            Field idFieldOfQClass = qClass.getDeclaredField(idField.getName());
            Field field = qClass.getDeclaredField(entityName);

            EntityPathBase<T> entityPathBase = (EntityPathBase<T>) field.get(qClass);
            NumberPath<Long> idPath = (NumberPath<Long>) idFieldOfQClass.get(entityPathBase);

            return new MetaEntity<T>()
                .setClassName(className)
                .setQClassName(qClassName)
                .setEntityPathBase(entityPathBase)
                .setIdPath(idPath)
                .setIdField(idField)
                .setEntityClass(entityClass)
                .setQClass(qClass);
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
