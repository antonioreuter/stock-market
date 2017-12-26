package com.stock.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableAutoConfiguration
@EnableJpaAuditing
@EntityScan(basePackages = {"com.stock"}, basePackageClasses = {Jsr310JpaConverters.class})
@EnableJpaRepositories(basePackages = {"com.stock.repositories"})
@EnableTransactionManagement
public class RepositoryConfig {
}