package com.company.jmixpm2_7_1.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.vault.core.VaultTemplate;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {
    private final VaultTemplate vaultTemplate;

    public DatabaseConfig(@Lazy VaultTemplate vaultTemplate) {
        this.vaultTemplate = vaultTemplate;
    }


    @Bean("dataSourceProperties")
    @Primary
    @Profile("default")
    @ConfigurationProperties("main.datasource")
    DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties("main.datasource.hikari")
    DataSource dataSource(final DataSourceProperties dataSourceProperties) {
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    @Bean("dataSourceProperties")
    @Primary
    @Profile("prod")
    DataSourceProperties dataSourcePropertiesVault() {
        DataSourceProperties properties = new DataSourceProperties();
        VaultWrapper vaultWrapper = new ObjectMapper().convertValue(
                vaultTemplate.read("secret/data/application/database/credentials").getData().get("data")
                , VaultWrapper.class
        );

        properties.setUrl(vaultWrapper.getUrl());
        properties.setUsername(vaultWrapper.getUsername());
        properties.setPassword(vaultWrapper.getPassword());

        return properties;
    }
}
