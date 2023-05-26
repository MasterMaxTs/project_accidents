package ru.job4j.accidents.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Класс конфигурации подключения к базе данных с помощью JdbcTemplate
 */
@Configuration
@EnableTransactionManagement
public class JdbcConfig {

    /**
     * Загружает пул соединений
     * @param driver имя класса драйвера
     * @param url URL
     * @param username username
     * @param password password
     * @return объект пула соединений
     */
    @Bean
    public DataSource ds(@Value("${spring.datasource.driver-class-name}") String driver,
                         @Value("${spring.datasource.url}") String url,
                         @Value("${spring.datasource.username}") String username,
                         @Value("${spring.datasource.password}") String password) {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName(driver);
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        return ds;
    }

    /**
     * Создаёт обёртку над JDBC для работы с БД
     * @param ds DataSource
     * @return объект обёртки
     */
    @Bean
    public JdbcTemplate jdbc(DataSource ds) {
        return new JdbcTemplate(ds);
    }

}
