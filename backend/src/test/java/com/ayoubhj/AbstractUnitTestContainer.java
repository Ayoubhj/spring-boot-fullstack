package com.ayoubhj;

import com.github.javafaker.Faker;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;


@Testcontainers
public abstract class AbstractUnitTestContainer {

    @Container
    protected static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("course-dao-unit-test")
            .withUsername("ayoub")
            .withPassword("ayoub");

    @BeforeAll
    static void beforeAll() {
        Flyway flyway = Flyway.configure().dataSource(POSTGRE_SQL_CONTAINER.getJdbcUrl(), POSTGRE_SQL_CONTAINER.getUsername(), POSTGRE_SQL_CONTAINER.getPassword()).load();
        flyway.migrate();
    }

    @DynamicPropertySource
    private static void registerDataSourceProperty(DynamicPropertyRegistry registy){
        registy.add(
                "spring.datasource.url",
                POSTGRE_SQL_CONTAINER::getJdbcUrl
        );
        registy.add(
                "spring.datasource.username",
                POSTGRE_SQL_CONTAINER::getUsername
        );
        registy.add(
                "spring.datasource.password",
                POSTGRE_SQL_CONTAINER::getPassword
        );
    }

    protected static DataSource getDataSource(){
        return   DataSourceBuilder.create()
                .driverClassName(POSTGRE_SQL_CONTAINER.getDriverClassName())
                .url(POSTGRE_SQL_CONTAINER.getJdbcUrl())
                .username(POSTGRE_SQL_CONTAINER.getUsername())
                .password(POSTGRE_SQL_CONTAINER.getPassword())
                .build();
    }

    protected static JdbcTemplate getJdbcTemplate(){
        return new JdbcTemplate(getDataSource());
    }

    protected static final Faker FAKER = new Faker();
}
