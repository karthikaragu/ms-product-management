package com.scm.product.management.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DBConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "auto-spare.scm.datasource")
    public DataSource dataSource(@Value("${UID}") String username, @Value("${PWD}") String password){
        return DataSourceBuilder.create()
                .username(username)
                .password(password)
                .build();
    }
}
