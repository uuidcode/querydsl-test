package com.github.uuidcode.querydsl.test.configuration;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.hibernate.dialect.MySQL5Dialect;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.github.jknack.handlebars.springmvc.HandlebarsViewResolver;
import com.github.uuidcode.querydsl.test.converter.GsonHttpMessageConverter;
import com.github.uuidcode.querydsl.test.dao.UserDao;
import com.github.uuidcode.querydsl.test.entity.EntityEntry;
import com.github.uuidcode.querydsl.test.strategy.DefaultPhysicalNamingStrategy;
import com.github.uuidcode.querydsl.test.util.CoreUtil;
import com.p6spy.engine.spy.P6SpyDriver;

@Configuration
@MapperScan(basePackageClasses = UserDao.class)
public class WebConfiguration implements WebMvcConfigurer {
    protected static Logger logger = LoggerFactory.getLogger(WebConfiguration.class);

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        if (logger.isDebugEnabled()) {
            List<String> convertList = converters.stream()
                .map(convert -> convert.getClass().getName())
                .collect(Collectors.toList());

            logger.debug(">>> configureMessageConverters converters: {}", CoreUtil.toJson(convertList));
        }

        converters.clear();
        GsonHttpMessageConverter gsonHttpMessageConverter = new GsonHttpMessageConverter();
        converters.add(gsonHttpMessageConverter);
    }

    /**
     * https://stackoverflow.com/questions/39884860/how-to-configure-spring-boot-pagination-starting-from-page-1-not-0
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setOneIndexedParameters(true);
        resolver.setFallbackPageable(PageRequest.of(0, 10));
        argumentResolvers.add(resolver);
    }

    @Bean
    public DataSource dataSource() {
        if (logger.isDebugEnabled()) {
            logger.debug(">>> dataSource");
        }

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
        properties.setProperty("hibernate.enable_lazy_load_no_trans", "true");
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

    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactory() {
        EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        cacheManagerFactoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
        cacheManagerFactoryBean.setShared(false);

        return cacheManagerFactoryBean;
    }

    @Bean
    public EhCacheCacheManager ehCacheCacheManager() {
        EhCacheCacheManager cacheManager = new EhCacheCacheManager();
        cacheManager.setCacheManager(ehCacheManagerFactory().getObject());
        cacheManager.setTransactionAware(false);

        return cacheManager;
    }
}
