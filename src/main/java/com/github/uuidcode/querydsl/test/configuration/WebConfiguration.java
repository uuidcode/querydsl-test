package com.github.uuidcode.querydsl.test.configuration;

import java.util.List;
import java.util.stream.Collectors;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.github.jknack.handlebars.springmvc.HandlebarsViewResolver;
import com.github.uuidcode.querydsl.test.converter.GsonHttpMessageConverter;
import com.github.uuidcode.querydsl.test.dao.UserDao;
import com.github.uuidcode.querydsl.test.util.CoreUtil;

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
}
