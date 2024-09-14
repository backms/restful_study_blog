package com.springblog.springblog.api.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Configuration
public class QueryDslConfig {

    @PersistenceContext
    public EntityManager em;

    @Bean
    public JPAQueryFactory jpaQeuryFactory() {
        return new JPAQueryFactory(em);
    }

}
