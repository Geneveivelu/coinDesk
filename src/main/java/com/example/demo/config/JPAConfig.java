package com.example.demo.config;

import com.zaxxer.hikari.HikariDataSource;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    entityManagerFactoryRef = "h2EntityManagerFactory",
    transactionManagerRef = "h2TransactionalManager",
    basePackages = {"com.example.demo"})
public class JPAConfig {

  @Primary
  @Bean("h2DriverDsp")
  @ConfigurationProperties("spring.datasource.h2")
  public DataSourceProperties h2DriverDsp() {
    return new DataSourceProperties();
  }

  @Primary
  @Bean("h2DataSource")
  @ConfigurationProperties("spring.datasource.h2.hikari")
  public HikariDataSource h2DataSource(@Qualifier("h2DriverDsp") DataSourceProperties h2DriverDataSourceProperties) {
    return h2DriverDataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
  }

  @Bean("h2EntityManagerFactory")
  public LocalContainerEntityManagerFactoryBean h2EntityManagerFactory(EntityManagerFactoryBuilder builder) {
    return builder
        .dataSource(h2DataSource(h2DriverDsp()))
        .packages("com.example.demo")
        .build();
  }

  @Primary
  @Bean("h2EntityManager")
  public EntityManager h2EntityManager(EntityManagerFactoryBuilder builder) {
    return h2EntityManagerFactory(builder).getObject().createEntityManager();
  }

  @Bean("h2TransactionalManager")
  public PlatformTransactionManager h2TransactionalManager(EntityManagerFactoryBuilder builder) {
    return new JpaTransactionManager(h2EntityManagerFactory(builder).getObject());
  }

}
