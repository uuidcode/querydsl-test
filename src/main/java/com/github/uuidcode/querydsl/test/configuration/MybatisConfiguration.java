package com.github.uuidcode.querydsl.test.configuration;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
public class MybatisConfiguration {
    @Autowired
    private DataSource dataSource;

    @Bean
    public SqlSessionFactory userSqlSessionFactory() throws Exception {
        Resource[] resources = new PathMatchingResourcePatternResolver()
            .getResources("classpath:mapper/**/*.xml");

        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(this.dataSource);
        factoryBean.setMapperLocations(resources);

        SqlSessionFactory factory = factoryBean.getObject();
        factory.getConfiguration().setMapUnderscoreToCamelCase(true);
        return factory;
    }
}
