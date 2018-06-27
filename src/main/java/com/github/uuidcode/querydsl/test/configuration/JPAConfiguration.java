package com.github.uuidcode.querydsl.test.configuration;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.dialect.MySQL5Dialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.github.uuidcode.querydsl.test.entity.EntityEntry;
import com.github.uuidcode.querydsl.test.strategy.DefaultPhysicalNamingStrategy;

@Configuration
public class JPAConfiguration {
    @Autowired
    private DataSource dataSource;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factoryBean
            = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(this.dataSource);
        factoryBean.setPackagesToScan(EntityEntry.class.getPackage().getName());

        JpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        factoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        factoryBean.setJpaProperties(this.additionalProperties());

        return factoryBean;
    }

    Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.dialect", MySQL5Dialect.class.getName());
        properties.setProperty("hibernate.physical_naming_strategy", DefaultPhysicalNamingStrategy.class.getName());
        properties.setProperty("hibernate.enable_lazy_load_no_trans", "true");
        properties.setProperty("org.hibernate.envers.audit_table_suffix", "_history");
        properties.setProperty("org.hibernate.envers.store_data_at_delete", "true");
        return properties;
    }
}
