package com.github.uuidcode.querydsl.test.configuration;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.hibernate.dialect.MySQL5Dialect;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.ViewResolver;

import com.github.jknack.handlebars.springmvc.HandlebarsViewResolver;
import com.github.uuidcode.querydsl.test.Entry;
import com.github.uuidcode.querydsl.test.dao.UserDao;
import com.github.uuidcode.querydsl.test.entity.EntityEntry;
import com.github.uuidcode.querydsl.test.strategy.DefaultPhysicalNamingStrategy;
import com.p6spy.engine.spy.P6SpyDriver;

@Configuration
@EnableJpaRepositories(basePackageClasses = Entry.class)
@ComponentScan(basePackageClasses = Entry.class)
@EnableTransactionManagement
@MapperScan(basePackageClasses = UserDao.class)
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class WebConfiguration {
    protected static Logger logger = LoggerFactory.getLogger(WebConfiguration.class);

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:p6spy:mysql://127.0.0.1:3306/querydsl?autoReconnect=true&useUnicode=true&characterEncoding=utf8&mysqlEncoding=utf8");
        dataSource.setUsername("root");
        dataSource.setPassword("rootroot");
        dataSource.setDriverClassName(P6SpyDriver.class.getName());
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factoryBean
            = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(this.dataSource());
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
        properties.setProperty("org.hibernate.envers.audit_table_suffix", "_history");
        properties.setProperty("org.hibernate.envers.store_data_at_delete", "true");
        return properties;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    @Bean
    public ViewResolver viewResolver() {
        if (logger.isDebugEnabled()) {
            logger.debug(">>> viewResolver");
        }

        HandlebarsViewResolver handlebarsViewResolver = new HandlebarsViewResolver();
        handlebarsViewResolver.setPrefix("classpath:/templates");
        handlebarsViewResolver.setSuffix(".hbs");
        handlebarsViewResolver.setCache(false);
        return handlebarsViewResolver;
    }

    @Bean
    public SqlSessionFactory userSqlSessionFactory() throws Exception {
        Resource[] resources = new PathMatchingResourcePatternResolver()
            .getResources("classpath:mapper/**/*.xml");

        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(this.dataSource());
        factoryBean.setMapperLocations(resources);

        SqlSessionFactory factory = factoryBean.getObject();
        factory.getConfiguration().setMapUnderscoreToCamelCase(true);
        return factory;
    }
}
