package com.yunseong.second_project.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"com.yunseong.second_project.member", "com.yunseong.second_project.product", "com.yunseong.second_project.category", "com.yunseong.second_project.order"})
public class JpaConfig {
}
