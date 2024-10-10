package com.springblog.springblog;

import com.springblog.springblog.api.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(AppConfig.class)
@SpringBootApplication
public class SpringBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBlogApplication.class, args);
    }

}
