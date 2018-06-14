package com.github.uuidcode.querydsl.test.strategy;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

import com.google.common.base.CaseFormat;

public class DefaultPhysicalNamingStrategy implements PhysicalNamingStrategy {
    @Override
    public Identifier toPhysicalCatalogName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return this.lowerUnderscore(identifier);
    }

    @Override
    public Identifier toPhysicalSchemaName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return this.lowerUnderscore(identifier);
    }

    @Override
    public Identifier toPhysicalTableName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return this.lowerUnderscore(identifier);
    }

    @Override
    public Identifier toPhysicalSequenceName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return this.lowerUnderscore(identifier);
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return this.lowerUnderscore(identifier);
    }

    public Identifier lowerUnderscore(Identifier identifier) {
        if (identifier == null) {
            return null;
        }

        String text = identifier.getText();

        if (text.equals(text.toUpperCase())) {
            text = text.toLowerCase();
        }

        String lowerUnderscore = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, text);
        return Identifier.toIdentifier(lowerUnderscore);
    }
}
